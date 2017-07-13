package test.mockito;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Mockito可以模拟接口和类，将他们所有的public、非final的方法都做模拟（即什么都不做，需要返回值的就返回默认值）<br/>
 * Mockito.mock(SomeClass.class) 可以模拟某个接口或类<br/>
 * 可以通过Mockito.when(xxx).thenReturn(yyy)方法指定对某个方法模拟返回值<br/>
 * 模拟抛出异常: Mockito.when(xxx).thenThrow(yyy)
 * <p>
 * Created by ziv on 2017/7/13.
 */
public class HelloWorldTest {

	@Test
	public void test() {
		System.out.println("模拟List接口:");
		// 创建模拟对象，参数可以是类，也可以是接口。将对其中的public、非final方法进行子类重写
		List mockList = Mockito.mock(List.class);
		// 打印模拟对象，发现其toString()方法被重写了
		System.out.println(mockList);
		String zero = "zero";
		// 设置某个方法在特定参数的返回值，参数支持通配：any(), anyInt()等
		Mockito.when(mockList.get(0)).thenReturn(zero);
		// 验证上个步骤设置的方法和参数的返回值是否为预期的值
		Assert.assertEquals(zero, mockList.get(0));
		// 对于没有设置的方法（或参数），将返回默认值
		Assert.assertNull(mockList.get(1));
	}

	@Test
	public void test_2_throw_exception() {
		System.out.println("指定添加对象时候跑出空指针异常：");
		List mockList = mock(List.class);

		// 方式1：抛出指定的异常类（非实例）
		when(mockList.add(0)).thenThrow(NullPointerException.class);
		try {
			mockList.add(0);
			assertTrue(false);
		} catch (NullPointerException e) {
			assertTrue(true);
		}

		// 方式2：抛出指定的异常对象（已经创建好的实例）
		when(mockList.get(anyInt())).thenThrow(new RuntimeException("不允许调用"));
		try {
			mockList.get(0);
			assertTrue(false);
		} catch (RuntimeException e) {
			System.err.println(e.getMessage());
			assertTrue(true);
		}
	}

	@Test
	public void test_3_第一次和第二次返回不同的值() {
		int first = 0;
		int second = 1;
		int result;

		List mockList = mock(List.class);
		when(mockList.size()).thenReturn(first).thenReturn(second);

		System.out.println(result = mockList.size());
		assertEquals(first, result);
		System.out.println(result = mockList.size());
		assertEquals(second, result);

		// 第3次及其之后的返回值都是最后一次设定的值了
		System.out.println(result = mockList.size());
		assertEquals(second, result);
		System.out.println(result = mockList.size());
		assertEquals(second, result);
	}

	@Test
	public void test_4_void_method() {
		List mockList = mock(List.class);

		// clear()方法没有返回值，如何对其模拟？

		// 当调用clear的时候，就抛出异常
		doThrow(RuntimeException.class).when(mockList).clear();
		try {
			mockList.clear();
			assertTrue(false);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(true);
		}

		// 当调用clear的时候，什么也不做（取消上一次设定的抛出异常动作）
		doNothing().when(mockList).clear();
		mockList.clear();

		// 当调用clear的时候打印日志
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				System.out.println("clear() is called.");
				return null;
			}
		}).when(mockList).clear();
		mockList.clear();
	}
}
