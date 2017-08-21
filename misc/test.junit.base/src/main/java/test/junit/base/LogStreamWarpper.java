package test.junit.base;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Created by ziv on 2017/8/21.
 */
public class LogStreamWarpper {
	public static boolean enableFileName = true;
	public static boolean enableThreadName = true;

	private static int maxThreadNameLength;
	private static int maxFileNameLength;

	public static PrintStream warp(final OutputStream outputStream) {
		return new PrintStream(outputStream) {

			private String getFormatedString(Object x) {
				StringBuilder sb = new StringBuilder();
				if (enableThreadName) {
					String threadName = Thread.currentThread().getName();
					maxThreadNameLength = Math.max(maxThreadNameLength, threadName.length());
					sb.append('[');
					sb.append(String.format("%" + maxThreadNameLength + "s", threadName));
					sb.append("] ");
				}
				if (enableFileName) {
					StackTraceElement[] stackTrace = new Exception().getStackTrace();
					int lineNumber = -1;
					String fileName = null;
					int depth = 2;
					for (int i = depth; -1 == lineNumber; i++) {
						lineNumber = stackTrace[i].getLineNumber();
						fileName = stackTrace[i].getFileName();
					}
					String target = fileName + ":" + lineNumber;
					maxFileNameLength = Math.max(maxFileNameLength, target.length());
					sb.append('(');
					sb.append(String.format("%" + maxFileNameLength + "s", target));
					sb.append(") - ");
				}
				sb.append(x);
				return sb.toString();
			}

			@Override
			public void println(String x) {
				super.println(getFormatedString(x));
			}

			@Override
			public void println(boolean x) {
				super.println(getFormatedString(x));
			}

			@Override
			public void println(int x) {
				super.println(getFormatedString(x));
			}

			@Override
			public void println(long x) {
				super.println(getFormatedString(x));
			}

			@Override
			public void println(double x) {
				super.println(getFormatedString(x));
			}

			@Override
			public void println(Object x) {
				super.println(getFormatedString(x));
			}
		};
	}
}
