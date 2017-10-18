package com.ziv.java.classload.multiversion.proxy;

import com.ziv.java.classload.multiversion.common.Executor;

/**
 * Created by ziv on 2017/10/18.
 */
public class ExecutorProxy implements Executor {

	private MyClassLoader myClassLoader;

	public ExecutorProxy(String version) {
		myClassLoader = new MyClassLoader(version);
	}

	@Override
	public void execute(String name) {
		try {
			// 这里是写死了包名与类名
			Class<?> aClass = myClassLoader.loadClass("com.ziv.java.classloader.executor.ExecutorImpl");
			Executor instance = (Executor) aClass.newInstance();
			instance.execute(name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
