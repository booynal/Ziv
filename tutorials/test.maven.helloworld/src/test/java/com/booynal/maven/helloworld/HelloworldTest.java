package com.booynal.maven.helloworld;

import org.junit.Assert;
import org.junit.Test;

public class HelloworldTest {
	@Test
	public void testSayHello() {
		Assert.assertEquals("Hello Maven.", new Helloworld().sayHello());
	}
}
