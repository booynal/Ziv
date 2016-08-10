package ziv.java8.basic;

import junit.framework.Assert;

import org.junit.Test;

import test.junit.base.BaseTest;

/**
 * <p>
 * 四、方法与构造函数引用
 * </p>
 * Java 8 允许你使用 :: 关键字来传递方法或者构造函数引用
 *
 * @author Booynal
 *
 */
public class MethodRefTest extends BaseTest {

	/**
	 * 前一节中的代码还可以通过静态方法引用来表示：
	 */
	@Test
	public void testStaticMethod() {
		System.out.println("1. 测试：通过【ClassName::staticMethodName】来引用静态方法");
		Converter<String, Integer> converter = Integer::valueOf;
		Integer converted = converter.convert("123");
		System.out.println(converted); // 123
		Assert.assertEquals(Integer.valueOf(123), converted);
	}

	@Test
	public void testObjectMethod() {
		System.out.println("2. 测试：通过【object::methodName】来引用对象方法(非静态方法)");
		Converter<String, Integer> converter = "Java"::indexOf;
		Integer converted = converter.convert("a");
		System.out.println(converted); // 1
		Assert.assertEquals(Integer.valueOf(1), converted);
	}

	@Test
	public void testConstructor() {
		System.out.println("3. 测试：通过【ClassName::new】来引用类的构造函数");
		// 这里我们使用构造函数引用来将他们关联起来，而不是实现一个完整的工厂：
		PersonFactory<Person> factory = Person::new;
		// 我们只需要使用 Person::new 来获取Person类构造函数的引用，
		// Java编译器会自动根据PersonFactory.create方法的签名来选择合适的构造函数。
		Person person = factory.create("Peter", "Parker");
		System.out.println(person.firstName + " " + person.lastName);
		Assert.assertEquals("Peter", person.firstName);
		Assert.assertEquals("Parker", person.lastName);
	}

	/**
	 * 首先我们定义一个包含多个构造函数的简单类：
	 */
	class Person {

		String firstName;
		String lastName;

		public Person() {
		}

		public Person(String firstName, String lastName) {
			this.firstName = firstName;
			this.lastName = lastName;
		}
	}

	/**
	 * 接下来我们指定一个用来创建Person对象的对象工厂接口：
	 */
	@FunctionalInterface
	interface PersonFactory<P extends Person> {

		P create(String firstName, String lastName);
	}

}
