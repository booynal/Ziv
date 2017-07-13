package test.mockito;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * PowerMock支持静态方法的模拟<br/>
 * PowerMock基于bytecode实现，而Mockito基于cglib实现<br/>
 * <p>
 * Created by ziv on 2017/7/13.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(StaticMockTestSay.class) // 将要被mock的类提前告诉powermock（如果是模拟本类则可省略），否则会报错
//@PrepareEverythingForTest // 慎用该注解，测试中发现报出StackOverFlow的异常
public class StaticMockTest {

	String hello = "hello";
	String world = "world";
	Object result;

	public static String staticMethod(String in) {
		System.out.println("调用staticMethod: " + in);
		return in;
	}

	@Test
	public void test() {
		System.out.println("在模拟之前返回真实值：");
		System.out.println(result = StaticMockTest.staticMethod(hello));
		Assert.assertEquals(hello, result);

		System.out.println("模拟之后返回模拟值：");
		PowerMockito.mockStatic(StaticMockTest.class);
		System.out.println(result = StaticMockTest.staticMethod(hello));
		Assert.assertEquals(null, result);
		PowerMockito.when(StaticMockTest.staticMethod(hello)).thenReturn(world);
		System.out.println(result = StaticMockTest.staticMethod(hello));
		Assert.assertEquals(world, result);
	}

	@Test
	public void test_mockOtherClass() {
		System.out.println("在模拟之前返回真实值：");
		System.out.println(result = StaticMockTestSay.say(hello));
		Assert.assertEquals(hello, result);

		System.out.println("模拟之后返回模拟值：");
		PowerMockito.mockStatic(StaticMockTestSay.class);
		PowerMockito.when(StaticMockTestSay.say(hello)).thenReturn(world); // 注意：必须使用@PrepareForTest注解在@RunWith(PowerMockRunner.class)下面声明需要模拟的类，否则这里会报错
		System.out.println(result = StaticMockTestSay.say(hello));
		Assert.assertEquals(world, result);
	}

}

class StaticMockTestSay {

	public static String say(String msg) {
		return msg;
	}

}
