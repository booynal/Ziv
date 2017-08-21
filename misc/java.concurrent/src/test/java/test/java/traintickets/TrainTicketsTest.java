package test.java.traintickets;

import org.junit.Test;
import test.junit.base.BaseTest;
import test.junit.base.LogStreamWarpper;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * java中线程同步机制主要采用synchronized关键字来实现，之后的java版本中也推出了ReentrantLock
 * 本程序用synchronized来实现一个火车票的购票逻辑
 * 假设在售票厅内设10个售票窗口,每个售票窗口相当于一个线程,这些线程的共同访问资源为售票厅的100张票。
 * Created by ziv on 2017/3/19.
 */
public class TrainTicketsTest extends BaseTest {

	private static final int WINDOW_COUNT = 10;
	private static final int TICKET_COUNT = 100;

	static {
		LogStreamWarpper.enableFileName = false;
	}

	private int remainder = TICKET_COUNT;

	@Test
	public void test_synchronized() throws InterruptedException {
		final CountDownLatch countDownLatch = new CountDownLatch(WINDOW_COUNT);
		final Object lock = new Object();
		for (int i = 0; i < WINDOW_COUNT; i++) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						while (true) {
							synchronized (lock) { // 此行代码为关键：在读取剩余票数之前加锁，使得该线程使用余票期间，不会有别的线程改动余票数
								if (hasTicket()) {
									Thread.sleep(1); // 模拟销售中的慢速
									doSell();
								} else {
									break;
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						countDownLatch.countDown();
					}
				}
			}, "t" + i);
			thread.start();
		}

		countDownLatch.await();
	}

	private boolean hasTicket() {
		return remainder > 0;
	}

	private void doSell() {
		remainder--;
		System.out.println("sell on ticket, remainder: " + remainder);
	}

	/**
	 * 测试点：用无锁的方式实现高并发<br/>
	 * 结果：失败率很高，但是失败后重试的机制是能够保证并发的一致性的
	 * @throws InterruptedException
	 */
	@Test
	public void test_atomic() throws InterruptedException {
		final CountDownLatch countDownLatch = new CountDownLatch(WINDOW_COUNT);
		final AtomicInteger atomicRemainder = new AtomicInteger(TICKET_COUNT);
		for (int i = 0; i < WINDOW_COUNT; i++) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						while (true) {
							int remainderCount = atomicRemainder.get();
							if (remainderCount > 0) {
								Thread.sleep(1);
								boolean b = atomicRemainder.compareAndSet(remainderCount, remainderCount - 1);
								if (b) {
									System.out.println(String.format("success(%s), remainder(%s)", remainderCount, atomicRemainder.get()));
								} else {
									System.err.println(String.format("failed, got '%s', but now is '%s'", remainderCount, atomicRemainder.get()));
								}
							} else {
								break;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						countDownLatch.countDown();
					}
				}
			}, "t" + i);
			thread.start();
		}

		countDownLatch.await();
	}

}
