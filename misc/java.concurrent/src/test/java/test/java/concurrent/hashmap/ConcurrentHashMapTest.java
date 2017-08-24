/**
 * ConcurrentHashMapTest.java
 */
package test.java.concurrent.hashmap;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

/**
 * @author ziv
 * @date 2017年8月24日 上午9:33:35
 */
public class ConcurrentHashMapTest {

	/**
	 * ConcurrentHashMap的bug：<br/>
	 * ConcurrentHashMap可以允许遍历的同时插入元素，单插入的元素不一定会被当前的迭代器遍历<br/>
	 * 遍历的元素是有序的（ASC顺序），例如，当前ConcurrentHashMap有a,和c两个key元素，当遍历到a元素（即将遍历c元素）的时候，插入了b(c之前)元素，此时b元素将不会被遍历，但如果插入d(c之后)元素则可以被本次迭代器遍历
	 *
	 */
	@Test
	public void test_iterateAndPut() throws InterruptedException {
		System.out.println("测试点：同时对ConcurrentHashMap做遍历和put，看看put进去的最新数据是否会被遍历到");
		final ConcurrentMap<String, String> testMap = new ConcurrentHashMap<>();
		testMap.put("a", "va");
		testMap.put("c", "vc");

		final CountDownLatch countDownLatch = new CountDownLatch(2);

		ExecutorService executorService = Executors.newFixedThreadPool(2);
		executorService.submit(() -> {
			for (Iterator<String> iterator = testMap.keySet().iterator(); iterator.hasNext();) {
				System.out.println(iterator.next());
				sleep(1000); // 遍历完一个元素后等待put元素
			}
			countDownLatch.countDown();
		});
		executorService.submit(() -> {
			sleep(100); // 等待遍历第一个元素
			testMap.put("b", "vb");
			testMap.put("d", "vd");
			countDownLatch.countDown();
		});

		countDownLatch.await();
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
