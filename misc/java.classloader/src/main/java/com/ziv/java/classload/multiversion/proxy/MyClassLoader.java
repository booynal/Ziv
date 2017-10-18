package com.ziv.java.classload.multiversion.proxy;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by ziv on 2017/10/18.
 */
public class MyClassLoader extends URLClassLoader {
	protected MyClassLoader(String version) {
		super(new URL[0], null); // 设置parent classloader为null
		loadResource(version);
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		Class<?> aClass = super.loadClass(name);
		System.err.println("loaded: " + aClass + "\t ClassLoader: " + aClass.getClassLoader());
		return aClass;
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		try {
			return super.findClass(name);
		} catch (ClassNotFoundException e) {
			return MyClassLoader.class.getClassLoader().loadClass(name);
		}
	}

	private void loadResource(String version) {
		String jarPath = System.getProperty("user.dir") + File.separator + "executor-impl" + File.separator + version;
		System.out.println("jarPath: " + jarPath);
		File dir = new File(jarPath);
		if (dir.exists() == false) {
			System.err.println("file not exists: " + dir.getAbsolutePath());
			return;
		}

		for (File file : dir.listFiles(f -> f.isFile() && f.getName().endsWith(".jar"))) {
			try {
				super.addURL(file.toURI().toURL());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}
}
