package com.ziv.java.classload;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ziv on 2017/10/16.
 */
public class ClassLoaderTest {

	public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		ClassLoader myClassLoader = new ClassLoader() {

			@Override
			public Class<?> loadClass(String name) throws ClassNotFoundException {
				if (null == name) {
					return null;
				}

				String path = name.substring(name.lastIndexOf('.') + 1) + ".class";
				System.err.println(path);
				InputStream stream = getClass().getResourceAsStream(path);
				System.err.println(stream);
				if (null == stream) {
					return super.loadClass(name);
				} else {
					try {
						byte[] b = new byte[stream.available()];
						stream.read(b);
						return defineClass(name, b, 0, b.length);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return null;
			}
		};

		Class<?> myClass = myClassLoader.loadClass(ClassLoaderTest.class.getName());
		Class<?> clazz = ClassLoaderTest.class;

		/*
		不同类加载器加载同一个类得到的两个Class对象对这些方法都不相等
		equals()
		isAssignableFrom()
		isInstance()
		instanceof关键字
		 */

		// 它们虽然类名称一样，但各自具有不同的hashcode，所以这两个Class对象并不相等，包括equals也不相等
		System.out.println("myClass: " + myClass + ", hash: " + myClass.hashCode());
		System.out.println("clazz  : " + clazz + ", hash: " + clazz.hashCode());
		System.out.println("myClass等于clazz吗？" + (myClass == clazz) + ", equals: " + myClass.equals(clazz));

		System.out.println("能从myClass中分配出myClass吗？" + myClass.isAssignableFrom(myClass));
		System.out.println("能从clazz中分配出myClass吗？" + myClass.isAssignableFrom(clazz));

		Object myObj = myClass.newInstance();
		System.out.println("myClass的实例是否属于clazz的实例？" + clazz.isInstance(myObj));
		System.out.println("myClass的实例是否instanceof clazz的类型呢？"+(myObj instanceof ClassLoaderTest)); // 该实验验证了：不同类加载器中的同一个类创建的实例并不instanceof另一个类加载中的类
	}
}
