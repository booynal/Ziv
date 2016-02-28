package ziv.java8.api;

import java.util.function.Consumer;

import org.junit.Test;

import ziv.java8.BaseTest;

/**
 * Consumer 接口表示执行在单个参数上的操作
 *
 * @author Booynal
 *
 */
public class ConsumerApiTest extends BaseTest {

	@Test
	public void test() {
		System.out.println("测试Consumer.accept(T)方法:");
		Consumer<String> myConsumer = (s) -> System.out.println("consumer: " + s);
		myConsumer.accept("Hello");
	}

}
