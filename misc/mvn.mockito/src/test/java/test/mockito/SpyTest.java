package test.mockito;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Spy: 只能对实例(而非类或接口)进行模拟，因为它将要保留原始对象的大部分实现方法，只有少数被when定义过的方法才是模拟的<br/>
 * 与mock的区别：mock是将所有的方法都进行模拟，spy只将需要的方法进行模拟<br/>
 * <p>
 * Created by ziv on 2017/7/14.
 */
public class SpyTest {

	@Test
	public void test() {
		List mock = mock(List.class);
		System.out.println(mock); // 打印模拟对象，toString已经被模拟了

		ArrayList<Object> spy = spy(new ArrayList<>());
		System.out.println(spy); // 打印模拟对象，toString保留了ArrayList得原始实现

	}

	@Test
	public void test_官方例子() {
		List list = new LinkedList();
		List spy = spy(list);

		int SIZE = 100;
		//optionally, you can stub out some methods:
		when(spy.size()).thenReturn(SIZE); // 对size方法进行模拟，使得调用size的时候返回我们设定的值

		//using the spy calls real methods
		spy.add("one"); // 没有对add方法进行模拟，所以add方法保持了原有的实现
		spy.add("two");

		//prints "one" - the first element of a list
		System.out.println(spy.get(0)); // get方法也是原有的实现

		//size() method was stubbed - 100 is printed
		int size = spy.size();
		System.out.println(size);
		Assert.assertEquals(SIZE, size);

		//optionally, you can verify
		verify(spy).add("one");
		verify(spy).add("two");
	}
}
