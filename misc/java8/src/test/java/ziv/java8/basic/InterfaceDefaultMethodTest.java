package ziv.java8.basic;

import org.junit.Assert;
import org.junit.Test;

import test.junit.base.BaseTest;

/**
 * <p>
 * 一、接口默认方法
 * </p>
 * Java 8允许我们给接口添加一个非抽象的方法实现，只需要使用 default关键字即可，这个特征又叫做扩展方法
 *
 * 在Java中只有单继承，如果要让一个类赋予新的特性，通常是使用接口来实现，<br/>
 * 在C++中支持多继承， 允许一个子类同时具有多个父类的接口与功能，<br/>
 * 在其他语言中，让一个类同时具有其他的可复用代码的方法叫做mixin。<br/>
 * 新的Java 8 的这个特新在编译器实现的角度上来说更加接近Scala的trait。 <br/>
 * 在C#中也有名为扩展方法的概念，允许给已存在的类型扩展方法，和Java 8的这个在语义上有差别。
 *
 * @author Booynal
 *
 */
public class InterfaceDefaultMethodTest extends BaseTest {

	/**
	 * 文中的formula被实现为一个匿名类的实例，该代码非常容易理解，6行代码实现了计算 sqrt(a * 100)。<br/>
	 * 在下一节中，我们将会看到实现单方法接口的更简单的做法。 <br/>
	 */
	@Test
	public void test() {
		Formula formula = new Formula() {

			@Override
			public double calculate(int a) {
				return sqrt(a * 100);
			}
		};

		System.out.println(formula.calculate(100)); // 100.0
		Assert.assertEquals(100.0d, formula.calculate(100), 0d);
		System.out.println(formula.sqrt(16)); // 4.0
		Assert.assertEquals(4.0d, formula.sqrt(16), 0d);
	}
}

/**
 * Formula接口在拥有calculate方法之外同时还定义了sqrt方法，实现了Formula接口的子类只需要实现一个calculate方法，<br/>
 * 默认方法sqrt将在子类上可以直接使用。
 *
 */
interface Formula {

	double calculate(int a);

	default double sqrt(int a) {
		return Math.sqrt(a);
	}
}
