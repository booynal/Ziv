package com.ziv.misc.storm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Created by ziv on 2017/8/21.
 */
public class LogUtil {

	private static final String logFileName = "logs/out.log";
	private static PrintStream out;

	static {
		init();
	}

	public static void init() {
		File file = new File(logFileName);
		try {
			if (file.exists() == false) {
				file.createNewFile();
			}
			System.out.println(String.format("将日志打印到: '%s'", file.getAbsolutePath()));
			out = new PrintStream(new FileOutputStream(file, true), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void log(String message) {
		out.println(message);
	}

}
