package ziv.java8.basic;

import junit.framework.Assert;

import org.junit.Test;

import test.junit.base.BaseTest;

/**
 * <p>
 * 三、函数式接口
 * </p>
 * “函数式接口”是指仅仅只包含一个抽象方法的接口，每一个该类型的lambda表达式都会被匹配到这个抽象方法。<br/>
 * 我们可以将lambda表达式当作任意只包含一个抽象方法的接口类型，确保你的接口一定达到这个要求，<br/>
 * 你只需要给你的接口添加 @FunctionalInterface 注解，编译器如果发现你标注了这个注解的接口有多于一个抽象方法的时候会报错的。
 *
 * @author Booynal
 *
 */
public class FunctionalInterfaceTest extends BaseTest {

	@Test
	public void testConverter() {
		Converter<String, Integer> converter = (from) -> Integer.valueOf(from);
		Integer converted = converter.convert("123");
		System.out.println(converted); // 123
		Assert.assertEquals(Integer.valueOf(123), converted);
	}
}

/**
 * 需要注意如果@FunctionalInterface如果没有指定，上面的代码也是对的。<br/>
 * 将lambda表达式映射到一个单方法的接口上，这种做法在Java 8之前就有别的语言实现，比如Rhino JavaScript解释器，<br/>
 * 如果一个函数参数接收一个单方法的接口而你传递的是一个function，Rhino 解释器会自动做一个单接口的实例到function的适配器，<br/>
 * 典型的应用场景有 org.w3c.dom.events.EventTarget 的addEventListener 第二个参数 EventListener
 *
 * @author Booynal
 *
 * @param <F>
 * @param <T>
 */
@FunctionalInterface
interface Converter<F, T> {

	T convert(F from);
}
