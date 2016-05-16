package test.locks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

/**
 * 1. wait和notify需要在synchronized内部使用，否则会抛出IllegalmonitorStateException<br>
 *
 * @author Booynal
 *
 */
public class ConcurrentTest extends BaseTest {

	private Object lock = new Object();
	private List<String> queue = new ArrayList<String>();

	@Test
	public void testProducerConsumer() throws InterruptedException {
		Thread consumer = new Thread(new Consumer(), "Consumer");
		Thread producer = new Thread(new Producer(), "Producer");

		consumer.setDaemon(true);
		producer.setDaemon(true);

		consumer.start();
		producer.start();

		Thread.sleep(5 * 1000);
	}

	private class Consumer implements Runnable {

		public void run() {
			while (true) {
				System.err.println("getting lock...");
				synchronized (lock) {
					System.err.println("got the lock.");
					if (queue.isEmpty()) {
						try {
							System.err.println("release the lock and waiting...");
							lock.wait();
							System.err.println("waked up.");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					for (String string : queue) {
						System.out.println(string);
					}
					queue.clear();
				}
				sleep();
			}
		}

	}

	private class Producer implements Runnable {

		public void run() {
			int i = 0;
			while (true) {
				System.err.println("getting lock...");
				synchronized (lock) {
					System.err.println("got the lock.");
					System.err.println("do its work...");
					queue.add("string" + i++);
					System.err.println("do its work done");
					System.err.println("notify...");
					lock.notify();
					System.err.println("notify done");
				}
				sleep();
			}
		}

	}

	private void sleep() {
		try {
			int millis = 5 * new Random().nextInt(1000);
			System.err.println("sleepping " + millis + " ms...");
			Thread.sleep(millis);
			System.err.println("sleeped done");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
