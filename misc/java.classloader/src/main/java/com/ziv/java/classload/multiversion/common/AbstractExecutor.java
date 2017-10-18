package com.ziv.java.classload.multiversion.common;

/**
 * Created by ziv on 2017/10/18.
 */
public class AbstractExecutor implements Executor {

	@Override
	public void execute(String name) {
		handle(new Handler() {
			@Override
			public void handle() {
				System.out.println("V: " + name);
			}
		});
	}

	protected void handle(Handler handler) {
		handler.call(getClass().getClassLoader());
	}

	protected abstract class Handler {
		protected void call(ClassLoader classLoader) {
			ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

			try {
				// 设置为与AbstractExecutor的实例相同的ClassLoader(自定义的ClassLoader)
				System.err.println("使用ClassLoader: " + classLoader);
				Thread.currentThread().setContextClassLoader(classLoader);
				handle();
			} finally {
				// 执行完任务之后，别忘记恢复原来的ClassLoader
				Thread.currentThread().setContextClassLoader(contextClassLoader);
			}

		}

		public abstract void handle();
	}
}
