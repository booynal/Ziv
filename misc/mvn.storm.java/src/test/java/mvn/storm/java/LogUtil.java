package mvn.storm.java;

import test.junit.base.LogStreamWarpper;

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
			LogStreamWarpper.enableFileName = false;
			out = LogStreamWarpper.warp(new FileOutputStream(file, true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void log(String message) {
		out.println(message);
	}

}
