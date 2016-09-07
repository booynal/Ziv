package test.java.concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 测试点：线程池的空闲线程是否会被回收
 *
 * @author Booynal
 *
 */
public class ThreadPoolIsReleaseIdleThreadTest {

	private static ThreadPoolExecutor executor;

	@BeforeClass
	public static void setUp() {
		ArrayBlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(5);
		ThreadFactory factory = new MyThreadFactory();
		CallerRunsPolicy handler = new ThreadPoolExecutor.CallerRunsPolicy();
		executor = new ThreadPoolExecutor(5, 10, 10000, TimeUnit.MILLISECONDS, workQueue, factory, handler);
	}

	@Test
	public void test() throws InterruptedException {
		for (int i = 0; i < 20; i++) {
			executor.execute(new MyTask(i + 1));
			System.out.println(String.format("poolSize: '%s', queueSize: '%s', completeTask: '%s'", executor.getPoolSize(), executor.getQueue().size(), executor.getCompletedTaskCount()));
		}

		for (int i = 0; i < 20; i++) {
			Thread.sleep(1000);
			System.out.println(String.format("poolSize: '%s', queueSize: '%s', completeTask: '%s'", executor.getPoolSize(), executor.getQueue().size(), executor.getCompletedTaskCount()));
		}

		executor.shutdown();
	}

}
