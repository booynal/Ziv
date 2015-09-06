package com.booynal.test.junit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@RunWith(MyTestRunner.class)
public class HelloWorldTest {

	@BeforeClass
	public static void setUp() {
//		for (Method method : HelloWorldTest.class.getDeclaredMethods()) {
//			System.err.println(method.getName());
//		}
	}
	
	@Test
	public void index1Order() {
		System.err.println("HelloWorldTest.order()");
	}
	
	@Test
	public void index2Xlacement() {
		System.err.println("HelloWorldTest.placement()");
	}
	
	@Test
	public void index3Executioin() {
		System.err.println("HelloWorldTest.executioin()");
	}
	
	@Test
	public void index4Fill() {
		System.err.println("HelloWorldTest.fill()");
	}
	
	@AfterClass
	public static void tearDown() {
//		Method[] declaredMethods = MethodSorter.getDeclaredMethods(HelloWorldTest.class);
//		for (Method method : declaredMethods) {
//			System.err.println(method.getName());
//		}
	}
	
}
