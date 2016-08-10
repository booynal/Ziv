package ziv.java8.basic;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import test.junit.base.BaseTest;

/**
 * <p>
 * 二、Lambda 表达式
 * </p>
 *
 * @author Booynal
 *
 */
public class LambdaTest extends BaseTest {

	@Test
	public void test() {
		List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");
		// 首先看看在老版本的Java中是如何排列字符串的：
		// 通常做法都是创建一个匿名的比较器对象然后将其传递给sort方法。
		Collections.sort(names, new Comparator<String>() {

			@Override
			public int compare(String a, String b) {
				return b.compareTo(a);
			}
		});

		// 在Java 8 中你就没必要使用这种传统的匿名对象的方式了，Java 8提供了更简洁的语法，lambda表达式：
		Collections.sort(names, (String a, String b) -> {
			return b.compareTo(a);
		});

		// 看到了吧，代码变得更段且更具有可读性，但是实际上还可以写得更短：
		Collections.sort(names, (String a, String b) -> b.compareTo(a));

		// 对于函数体只有一行代码的，你可以去掉大括号{}以及return关键字，但是你还可以写得更短点：
		// Java编译器可以自动推导出参数类型，所以你可以不用再写一次类型。
		Collections.sort(names, (a, b) -> b.compareTo(a));
	}

}
