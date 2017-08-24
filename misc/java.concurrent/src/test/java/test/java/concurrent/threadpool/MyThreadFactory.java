package test.java.concurrent.threadpool;

import java.util.concurrent.ThreadFactory;

public class MyThreadFactory implements ThreadFactory {

	private int count;

	@Override
	public Thread newThread(Runnable r) {
		return new Thread(r, "thread_" + ++count);
	}
}
