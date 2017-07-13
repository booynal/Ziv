package test.mockito;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * 方法调用次数的验证
 * Created by ziv on 2017/7/14.
 */
public class MethodVerifyTest {

	@Test
	public void test() {
		List mockList = mock(List.class);

		String once = "once";
		String twice = "twice";
		String three = "three";

		mockList.add(once);

		mockList.add(twice);
		mockList.add(twice);

		mockList.add(three);
		mockList.add(three);
		mockList.add(three);

		// 开始验证方法调用的次数
		verify(mockList).add(once); // 调用了一次，省略次数的设定
		verify(mockList, times(1)).add(once); // 调用了一次
		verify(mockList, times(2)).add(twice); // 调用了2次
		verify(mockList, times(3)).add(three); // 调用了3次

		verify(mockList, atLeastOnce()).add(once); // 至少调用了1次
		verify(mockList, atLeastOnce()).add(twice); // 至少调用了1次
		verify(mockList, atLeastOnce()).add(three); // 至少调用了1次

		verify(mockList, atLeast(2)).add(twice); // 至少调用了2次
		verify(mockList, atLeast(2)).add(three); // 至少调用了2次

		verify(mockList, atLeast(3)).add(three); // 至少调用了3次

		verify(mockList, atMost(5)).add(once); // 最多调用了5次
		verify(mockList, atMost(5)).add(twice); // 最多调用了5次
		verify(mockList, atMost(5)).add(three); // 最多调用了5次

		// 通配参数的验证
		verify(mockList, times(6)).add(anyString()); // add方法调用了6次
	}

	@Test
	public void test_2_anyInt() {
		List mockList = mock(List.class);

		mockList.get(0);
		mockList.get(1);

		verify(mockList).get(0);
		verify(mockList, times(1)).get(1); // 这两种方式等价，Mockito.times(1)可以设置为多次
		// 参数匹配器：anyInt()可以匹配任意的int的参数，当前所有的int参数只有0和1，所有times(2)是能够通过验证的
		verify(mockList, times(2)).get(anyInt());

		try {
			// 验证一个从未被调用过的方法将抛出错误
			verify(mockList).get(2);
			assertTrue(false);
		} catch (Error e) {
			e.printStackTrace();
			assertTrue(true);
		}
	}
}
