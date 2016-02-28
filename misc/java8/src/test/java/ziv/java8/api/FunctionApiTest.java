package ziv.java8.api;

import java.util.function.Function;

import org.junit.Assert;
import org.junit.Test;

import ziv.java8.BaseTest;

/**
 * Function 接口有一个参数并且返回一个结果，并附带了一些可以和其他函数组合的默认方法（compose, andThen）
 *
 * @author Booynal
 *
 */
public class FunctionApiTest extends BaseTest {

	@Test
	public void test() {
		System.out.println("测试Function.apply(T)方法:");
		Function<String, Integer> toInteger = Integer::valueOf;
		Assert.assertEquals(123, toInteger.apply("123").intValue()); // 123

		Function<String, String> backToString = toInteger.andThen(String::valueOf);
		String apply = backToString.apply("123"); // "123"
		System.out.println(apply);
		Assert.assertEquals("123", apply);

	}

}
