package com.booynal.test.junit;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

public class MyTestRunner extends BlockJUnit4ClassRunner {

	public MyTestRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}

	@Override
	public void run(RunNotifier notifier) {
		System.err.println("MyTestRunner.run()");
		super.run(notifier);
		System.err.println("MyTestRunner.run()end");
	}

	@Override
	protected void runChild(FrameworkMethod method, RunNotifier notifier) {
		System.err.println("MyTestRunner.runChild()");
		super.runChild(method, notifier);
		System.err.println("MyTestRunner.runChild()end");
	}

}
