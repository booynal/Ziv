package test.junit.base;

import org.junit.BeforeClass;

public class BaseTest {

	@BeforeClass
	public static void setUpSystemOut() {
		System.setOut(LogStreamWarpper.warp(System.out));
		System.setErr(LogStreamWarpper.warp(System.err));
	}

}
