package test.java.volatile_test;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * 证明volatile关键字不能解决并发问题的代码
 */
public class VolatileTest {

	private volatile int counter;
	private CountDownLatch countDownLatch;

	@Test
	public void test() throws InterruptedException {
		int threadCount = 1000;
		countDownLatch = new CountDownLatch(threadCount);
		System.out.println(String.format("测试：用'%s'个线程，每个线程都将counter的值加1，最后期待counter的值为: '%s'", threadCount, threadCount));
		for (int i = 0; i < threadCount; i++) {
			new Thread(() -> add()).start();
		}
		countDownLatch.await();
		System.out.println(String.format("最终结果为: '%s'", counter));
		Assert.assertEquals("结果不相等，则证明成功", threadCount, counter);
	}

	private void add() {
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
		}
		counter++;
		countDownLatch.countDown();
	}
}
