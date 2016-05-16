package test.locks;

import java.io.PrintStream;

import org.junit.BeforeClass;


public class BaseTest {

	@BeforeClass
	public static void setUp() {
		System.setErr(new PrintStream(System.err) {
	
			@Override
			public void print(String s) {
				String name = Thread.currentThread().getName();
				super.print(String.format("[%s] -\t%s", name, s));
			}
	
		});
	}

}
