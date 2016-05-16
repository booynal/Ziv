package test.locks;

import org.junit.Test;

public class WaitNotifyTest extends BaseTest {

	private Object lock = new Object();

	@Test(expected = IllegalMonitorStateException.class)
	public void testWait不在Synchronized中() throws InterruptedException {
		lock.wait();
	}

	@Test
	public void testWait在Synchronized中() throws InterruptedException {
		synchronized (lock) {
			lock.wait(10);
		}
	}

	@Test(expected = IllegalMonitorStateException.class)
	public void testNotify不在Synchronized中() throws InterruptedException {
		lock.notify();
	}

	@Test
	public void testNotify在Synchronized中() throws InterruptedException {
		synchronized (lock) {
			lock.notify();
		}
	}

	@Test(expected = IllegalMonitorStateException.class)
	public void testNotifyAll不在Synchronized中() throws InterruptedException {
		lock.notifyAll();
	}

	@Test
	public void testNotifyAll在Synchronized中() throws InterruptedException {
		synchronized (lock) {
			lock.notifyAll();
		}
	}
}
