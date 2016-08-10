package ziv.java8.basic;

import org.junit.Assert;
import org.junit.Test;

import test.junit.base.BaseTest;

/**
 * <p>
 * 五、Lambda 作用域
 * </p>
 * 在lambda表达式中访问外层作用域和老版本的匿名对象中的方式很相似。<br/>
 * 你可以直接访问标记了final的外层局部变量，或者实例的字段以及静态变量。
 *
 * @author Booynal
 *
 */
public class LambdaScopeTest extends BaseTest {

	/**
	 * <p>
	 * 六、访问局部变量
	 * </p>
	 * 我们可以直接在lambda表达式中访问外层的final局部变量：
	 */
	@Test
	public void testFinalLocalVariable() {
		System.out.println("1. 测试：在Lambda表达式中访问外层的final局部变量");
		final int num = 1;
		Converter<Integer, String> stringConverter = (from) -> String.valueOf(from + num);
		String converted = stringConverter.convert(2);
		System.out.println(converted); // 3
		Assert.assertEquals("3", converted);
	}

	@Test
	public void testNonFinalLocalVariable() {
		System.out.println("2. 测试：在Lambda表达式中访问外层的【非】final局部变量");
		int num = 1;
		Converter<Integer, String> stringConverter = (from) -> String.valueOf(from + num);
		String converted = stringConverter.convert(2);
		System.out.println(converted); // 3
		Assert.assertEquals("3", converted);
	}

	@Test
	public void testCannotModify() {
		System.out.println("3. 测试：在Lambda表达式【中】不可以修改外层的局部变量(即隐性的具有final的语义)");
		System.out.println("4. 测试：在Lambda表达式【之后】不可以修改外层的局部变量(即隐性的具有final的语义)");
		// int num = 1;
		// Converter<Integer, String> stringConverter = (from) ->
		// String.valueOf(from + ++num); // 此处(++num)不能修改num的值
		// num = 3; // 此处不能修改num的值
	}

	int outerNum;
	static int outerStaticNum;

	/**
	 * <p>
	 * 七、访问对象字段与静态变量
	 * </p>
	 * lambda内部对于实例的字段以及静态变量是即可读又可写。该行为和匿名对象是一致的：
	 */
	@Test
	public void testAccessObjectField() {
		Converter<Integer, String> stringConverter1 = (from) -> {
			outerNum = 23;
			return String.valueOf(from);
		};
		stringConverter1.convert(5);
		Assert.assertEquals(23, outerNum);

		Converter<Integer, String> stringConverter2 = (from) -> {
			outerStaticNum = 72;
			return String.valueOf(from);
		};
		stringConverter2.convert(5);
		Assert.assertEquals(72, outerStaticNum);

	}
}
