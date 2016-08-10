package ziv.java8.api;

import java.util.NoSuchElementException;
import java.util.Optional;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import test.junit.base.BaseTest;

/**
 * Optional 不是函数式接口，这是个用来防止NullPointerException异常的辅助类型<br/>
 * Optional 被定义为一个简单的容器，其值可能是null或者不是null。<br/>
 * 在Java 8之前一般某个函数应该返回非空对象但是偶尔却可能返回了null，而在Java 8中，不推荐你返回null而是返回Optional。
 *
 * @author Booynal
 *
 */
public class OptionalApiTest extends BaseTest {

	Optional<String> optional1;
	Optional<String> optional2;

	@Before
	public void setUp() {
		optional1 = Optional.of("Test value");
		optional2 = Optional.ofNullable(null);
	}

	@Test
	public void testIsPresent() {
		System.out.println("判断值是否存在(非空):");
		System.out.println(optional1.isPresent());
		Assert.assertTrue(optional1.isPresent());
		System.out.println(optional2.isPresent());
		Assert.assertFalse(optional2.isPresent());
	}

	@Test
	public void testGetNormal() {
		System.out.println("获取值:");
		System.out.println(optional1.get());
		Assert.assertEquals("Test value", optional1.get());
	}

	@Test(expected = NoSuchElementException.class)
	public void testGetWithException() {
		System.out.println("获取optional2的值:(会抛出NoSuchElementException异常)");
		try {
			System.out.println(optional2.get());
		} catch (NoSuchElementException e) {
			throw e;
		}
	}

	@Test
	public void testOrElse() {
		System.out.println("测试orElse(T):");
		String other = "only if the value is null, then return this value.";
		System.out.println(optional1.orElse(other));
		Assert.assertEquals("Test value", optional1.orElse(other));
		System.out.println(optional2.orElse(other));
		Assert.assertEquals(other, optional2.orElse(other));
	}

	@Test
	public void testIfPresent() {
		System.out.println("测试IfPresent():");
		optional1.ifPresent(System.out::println);
		optional1.ifPresent((actual) -> Assert.assertEquals("Test value", actual));
		boolean[] isOptoinal1Invoke = new boolean[] { false };
		optional1.ifPresent((actual) -> isOptoinal1Invoke[0] = true);
		Assert.assertTrue(isOptoinal1Invoke[0]);

		optional2.ifPresent(System.out::println);
		boolean[] isOptoinal2Invoke = new boolean[] { false };
		optional2.ifPresent((actual) -> isOptoinal2Invoke[0] = true);
		Assert.assertFalse(isOptoinal2Invoke[0]);
	}
}
