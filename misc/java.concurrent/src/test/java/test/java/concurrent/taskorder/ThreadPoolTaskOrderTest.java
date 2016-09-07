package test.java.concurrent.taskorder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.junit.Test;

import test.java.concurrent.MyTask;
import test.java.concurrent.MyThreadFactory;

/**
 * 测试点：线程池中线程只有1个的、队列容量只有1的情况下，如何保证加入进来的任务是按照进入进来的顺序执行<br/>
 * 如何才能保证FIFO：加入队列的时候如果满了就等待，另外阻塞队列最好是公平的(FIFO)，<br/>
 * 例如ArrayBlockingQueue就可以在构造函数上设置fair参数
 *
 * @author ziv
 *
 */
public class ThreadPoolTaskOrderTest {

	private static ThreadPoolExecutor executor;

	@BeforeClass
	public static void setUp() {
		// 此处队列容量为1，并且公平模式(FIFO)，但是测试如何不公平好像也可以保证FIFO
		ArrayBlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(1, true);
		ThreadFactory factory = new MyThreadFactory();
		RejectedExecutionHandler handler = new WaitPolicy(); // 此处也由CallerRunsPolicy改为WaitPolicy，才能保证他们的顺序
		executor = new ThreadPoolExecutor(1, 1, 200, TimeUnit.MILLISECONDS, workQueue, factory, handler);
	}

	@Test
	public void test() throws InterruptedException {
		for (int i = 0; i < 5; i++) {
			executor.execute(new MyTask(i + 1));
			System.out.println(String.format("poolSize: '%s', queueSize: '%s', completeTask: '%s'", executor.getPoolSize(), executor.getQueue().size(), executor.getCompletedTaskCount()));
		}
		Thread.sleep(2000);
		executor.shutdown();
		System.out.println("Done");
	}
}
