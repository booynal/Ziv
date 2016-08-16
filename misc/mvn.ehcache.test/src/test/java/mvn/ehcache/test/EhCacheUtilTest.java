package mvn.ehcache.test;

import org.junit.Test;

import test.ehcache.utils.EhCacheUtil;
import test.junit.base.BaseTest;

/**
 * 测试多线程并发的缓存与获取值
 *
 * @author ziv
 *
 */
public class EhCacheUtilTest extends BaseTest {

	private String cacheName = "testCacheName";

	@Test
	public void test() throws InterruptedException {
		String key = "testKey";
		String value = "testValue";
		EhCacheUtil.setObject(cacheName, key, value);
		Object object = EhCacheUtil.getObject(cacheName, key);
		System.out.println(object);
	}

	@Test
	public void test_thread() throws InterruptedException {
		String aKey = "kA";
		String aValue = "vA";
		String bKey = "kB";
		String bValue = "vB";

		// 先加载类以及初始化一遍，以使得以下每个线程更均匀(避免第一个线程去坐初始化，而影响该线程的进度)
		EhCacheUtil.setObject(cacheName, "", "");

		new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				EhCacheUtil.setValue(cacheName, aKey + i, aValue + i);
			}
		}, "set1").start();

		new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				EhCacheUtil.setValue(cacheName, bKey + i, bValue + i);
			}
		}, "set2").start();

		new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				String key = aKey + i;
				System.out.println(key + "\t= " + EhCacheUtil.getValue(cacheName, key));
			}
		}, "get1").start();

		new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				String key = bKey + i;
				System.out.println(key + "\t= " + EhCacheUtil.getValue(cacheName, key));
			}
		}, "get2").start();

		Thread.sleep(1000);
		System.out.println("done");
	}

}
