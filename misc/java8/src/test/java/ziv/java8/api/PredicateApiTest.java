package ziv.java8.api;

import java.util.Objects;
import java.util.function.Predicate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import test.junit.base.BaseTest;

/**
 * Predicate 接口只有一个参数，返回boolean类型。<br/>
 * 该接口包含多种默认方法来将Predicate组合成其他复杂的逻辑（比如：与，或，非）
 *
 * @author Booynal
 *
 */
public class PredicateApiTest extends BaseTest {

	Predicate<String> predicate;

	@Before
	public void setUp() {
		predicate = (s) -> s.length() > 2;

	}

	@Test
	public void testTest() {
		System.out.println("测试Predicate.test(T)方法:");
		boolean lengthMoreThanOne = predicate.test("foo"); // true
		System.out.println(lengthMoreThanOne);
		Assert.assertTrue(lengthMoreThanOne);
	}

	@Test
	public void testNegate() {
		boolean notLengthMoreThanOne = predicate.negate().test("foo"); // false
		System.out.println(notLengthMoreThanOne);
		Assert.assertFalse(notLengthMoreThanOne);
	}

	@Test
	public void testAnd() {
		System.out.println("测试and方法。判断：" + "长度>2并且最后一个字符是o");
		Predicate<String> and = predicate.and((s) -> s.endsWith("o"));
		System.out.println(and.test("foo"));
		Assert.assertTrue(and.test("foo"));
		System.out.println(and.test("oof"));
		Assert.assertFalse(and.test("oof"));
	}

	@Test
	public void testOr() {
		System.out.println("测试or方法。判断：" + "长度>2或者第一个字符是f");
		Predicate<String> or = predicate.or((s) -> s.startsWith("f"));
		System.out.println(or.test("too"));
		Assert.assertTrue(or.test("too"));
		System.out.println(or.test("fo"));
		Assert.assertTrue(or.test("fo"));
		System.out.println(or.test("o"));
		Assert.assertFalse(or.test("o"));
	}

	/**
	 * 举例如下
	 */
	@SuppressWarnings("unused")
	@Test
	public void test() {
		Predicate<Boolean> nonNull = Objects::nonNull;
		Predicate<Boolean> isNull = Objects::isNull;
		Predicate<String> isEmpty = String::isEmpty;
		Predicate<String> isNotEmpty = isEmpty.negate();

	}

}
