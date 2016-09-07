package test.java.concurrent;

import java.util.concurrent.ThreadFactory;

final class MyThreadFactory implements ThreadFactory {

	private int count;

	@Override
	public Thread newThread(Runnable r) {
		return new Thread(r, "thread_" + ++count);
	}
}
