package test.mockito;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Created by ziv on 2017/7/20.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ForPowerMockTestClass.class)
public class PowerMockTest {

	@Before
	public void initMock() {
		PowerMockito.mockStatic(ForPowerMockTestClass.class);
	}

	@Test
	public void test() {
		Object result;
		final String testMsg = "testMsg";
		final int testInt = 999;


		PowerMockito.when(ForPowerMockTestClass.staticMethod(Mockito.anyString())).thenReturn(testMsg);
		System.out.println(result = ForPowerMockTestClass.staticMethod("aaa"));
		Assert.assertEquals(testMsg, result);

		PowerMockito.when(ForPowerMockTestClass.defaultModifyMethod(Mockito.anyInt())).thenReturn(testInt);
		System.out.println(result = ForPowerMockTestClass.defaultModifyMethod(0));
		Assert.assertEquals(testInt, result);

		try {
			PowerMockito.doReturn(testMsg).when(ForPowerMockTestClass.class, "innerGetStatus"); // 私有方法只能写字符串类型的方法名称
			PowerMockito.when(ForPowerMockTestClass.publicGetStatus()).thenCallRealMethod(); // 此处放该方法去调用私有方法
			System.out.println(result = ForPowerMockTestClass.publicGetStatus());
			Assert.assertEquals(testMsg, result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		PowerMockito.when(ForPowerMockTestClass.finalMethod(Mockito.anyString())).thenReturn(testMsg);
		System.out.println(result = ForPowerMockTestClass.finalMethod("aaa"));
		Assert.assertEquals(testMsg, result);

	}

}

class ForPowerMockTestClass {
	public static String staticMethod(String msg) {
		System.out.println("我不会被执行的");
		return msg;
	}

	static int defaultModifyMethod(int i) {
		System.out.println("我不会被执行的");
		return i;
	}

	private static String innerGetStatus() {
		System.out.println("我不会被执行的");
		return "status";
	}

	public static String publicGetStatus() {
		System.out.println("我会被执行，且调用私有的innerGetStatus()方法");
		return innerGetStatus();
	}

	protected static final String finalMethod(String string) {
		System.out.println("我不会被执行的");
		return string;
	}
}
