package mvn.spring.helloworld;

import mvn.spring.helloworld.subpackage.SubPackageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringHelloworldApplication {

	@Autowired
	private HelloworldService helloworldService;
	private static ClassPathXmlApplicationContext context;

	static {
		init();
	}

	public static void main(String[] args) {
		test1();
		test2();
		test3();
	}

	/**
	 * 初始化Spring
	 */
	private static void init() {
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
		context.start();
	}

	/**
	 * 测试点：context.getBean()是否能够正确的获取到bean
	 */
	private static void test1() {
		HelloworldService service = context.getBean(HelloworldService.class);
		System.out.println(service);
		String status = service.sayHello("test1");
		System.out.println(status);
	}

	/**
	 * 测试点：@Autowired是否能够正确的注入bean
	 */
	private static void test2() {
		SpringHelloworldApplication service = context.getBean(SpringHelloworldApplication.class);
		System.out.println(service);
		System.out.println(service.helloworldService);
		String status = service.helloworldService.sayHello("test2");
		System.out.println(status);
	}

	/**
	 * 测试点：测试Spring是否能够扫描子包
	 */
	private static void test3() {
		SubPackageService service = context.getBean(SubPackageService.class);
		System.out.println(service);
		service.say("test3");
	}

}
