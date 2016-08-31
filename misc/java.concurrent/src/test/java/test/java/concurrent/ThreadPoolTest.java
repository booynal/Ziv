package test.java.concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.junit.Test;

public class ThreadPoolTest {

	private static ThreadPoolExecutor executor;

	@BeforeClass
	public static void setUp() {
		ArrayBlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(5);
		ThreadFactory factory = new ThreadFactory() {

			int c;

			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "thread_" + ++c);
			}
		};
		CallerRunsPolicy handler = new ThreadPoolExecutor.CallerRunsPolicy();
		executor = new ThreadPoolExecutor(5, 10, 200, TimeUnit.MILLISECONDS, workQueue, factory, handler);
	}

	@Test
	public void test() {
		for (int i = 0; i < 20; i++) {
			executor.execute(new MyTask(i + 1));
			System.out.println(String.format("poolSize: '%s', queueSize: '%s', completeTask: '%s'", executor.getPoolSize(), executor.getQueue().size(), executor.getCompletedTaskCount()));
		}
		executor.shutdown();
	}

	static class MyTask implements Runnable {

		private String taskName;

		public MyTask(int num) {
			taskName = "task_" + num;
		}

		@Override
		public void run() {
			System.out.println(Thread.currentThread().getName() + " 正在执行: " + taskName);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(taskName + " 执行完毕");
		}

	}

}
