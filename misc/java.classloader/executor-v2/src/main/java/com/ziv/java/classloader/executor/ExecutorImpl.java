package com.ziv.java.classloader.executor;

import com.ziv.java.classload.multiversion.common.AbstractExecutor;

/**
 * Created by ziv on 2017/10/18.
 */
public class ExecutorImpl extends AbstractExecutor {
	@Override
	public void execute(final String name) {
		handle(new Handler() {
			@Override
			public void handle() {
				System.out.println("v2: " + name);
			}
		});
	}
}
