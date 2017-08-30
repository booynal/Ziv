package test.mockito;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * 说明：PowerMock如何禁用静态块的代码呢？<br/>
 * 可以通过注解@SuppressStaticInitializationFor("a.b.c.ClassName")的方式禁用
 * Created by ziv on 2017/8/14.
 */
@RunWith(PowerMockRunner.class) // 必须用PowerMockRunner来运行
@PrepareForTest(StaticBlockMockTestForTest.class) // 可选该注释，作用是模拟指定类的静态方法，与是否禁用静态块没有直接关系
@SuppressStaticInitializationFor("test.mockito.StaticBlockMockTestForTest") // 这里就是禁用指定类的静态块和静态初始化的代码
public class StaticBlockMockTest {

	@Test
	public void test() {
		StaticBlockMockTestForTest staticBlockMockTestForTest = Mockito.spy(new StaticBlockMockTestForTest());
		String msg = "test message";
		String say = staticBlockMockTestForTest.say(msg);
		Assert.assertEquals(msg, say);
	}
}

class StaticBlockMockTestForTest {
	static {
		System.out.println("我不会被执行，因为静态块被压制了");
	}

	static boolean isOk() {
		System.out.println("isOk也不会被执行，因为静态初始化被压制了");
		return true;
	}

	private final static boolean ok = isOk();

	String say(String msg) {
		System.out.println(msg);
		return msg;
	}
}
