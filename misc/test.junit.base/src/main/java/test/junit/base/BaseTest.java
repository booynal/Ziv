package test.junit.base;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.PrintStream;

public class BaseTest {

	protected static boolean enableThreadName = true;
	protected static boolean enableFileName = true;

	private static int maxThreadNameLength;
	private static int maxFileNameLength;

	@BeforeClass
	public static void setUpSystemOut() {
		System.setOut(warp(System.out));
		System.setErr(warp(System.err));
	}

	private static PrintStream warp(final PrintStream printStream) {
		return new PrintStream(printStream) {

			private String getFormatedString(Object x) {
				StringBuilder sb = new StringBuilder();
				if (enableThreadName) {
					String threadName = Thread.currentThread().getName();
					maxThreadNameLength = Math.max(maxThreadNameLength, threadName.length());
					sb.append('[');
					sb.append(String.format("%" + maxThreadNameLength + "s", threadName));
					sb.append("]\t");
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
					sb.append(")\t");
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
