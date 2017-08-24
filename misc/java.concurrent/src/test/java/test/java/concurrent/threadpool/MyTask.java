package test.java.concurrent.threadpool;

public class MyTask implements Runnable {

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
