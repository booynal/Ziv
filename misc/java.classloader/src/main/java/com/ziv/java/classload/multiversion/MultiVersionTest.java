package com.ziv.java.classload.multiversion;

import com.ziv.java.classload.multiversion.proxy.ExecutorProxy;

/**
 * Created by ziv on 2017/10/18.
 */
public class MultiVersionTest {

	public static void main(String[] args) {
		if (args.length <= 0) {
			System.out.println("将按照顺序调用v1和v2版本");
			new ExecutorProxy("v1").execute("Ziv");
			new ExecutorProxy("v2").execute("Ivy");
		} else {
			new ExecutorProxy(args[0]).execute("Ziv");
		}
	}
}
