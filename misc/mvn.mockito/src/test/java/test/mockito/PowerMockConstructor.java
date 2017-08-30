package test.mockito;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * PowerMock 支持对构造函数的模拟<br/>
 * 通过PowerMockito.whenNew(Class)来实现
 * Created by ziv on 2017/8/30.
 */
@RunWith(PowerMockRunner.class) // 必须用PowerMockRunner来运行
public class PowerMockConstructor {


	@Test
	public void test() {
		try {
			PowerMockito.whenNew(PowerMockConstructorForTest.class).withAnyArguments().thenAnswer(ivk -> {
				System.out.println("arg: " + ivk.getArgumentAt(0, String.class));
				return Mockito.mock(PowerMockConstructorForTest.class);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(new PowerMockConstructorForTest("test message."));
	}

	static class PowerMockConstructorForTest {

		public PowerMockConstructorForTest(String msg) {
			System.out.println("String构造方法被执行，参数: " + msg); // 该语句不会被执行
		}
	}
}
