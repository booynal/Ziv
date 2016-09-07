package test.java.concurrent.taskorder;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class WaitPolicy implements RejectedExecutionHandler {

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		BlockingQueue<Runnable> queue = executor.getQueue();
		if (null != queue) {
			try {
				queue.put(r);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
