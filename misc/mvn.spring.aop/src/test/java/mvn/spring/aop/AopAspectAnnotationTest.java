package mvn.spring.aop;

import mvn.spring.aop.aspect.MyAspectAnnotationService;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import test.junit.base.BaseTest;

/**
 * @author Booynal
 *
 */
public class AopAspectAnnotationTest extends BaseTest {

	private static MyAspectAnnotationService service;

	@BeforeClass
	public static void setUp() throws Exception {
		try {
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("mvn-spring-aop-annotation-Aspect-test.xml");
			context.start();
			service = context.getBean(MyAspectAnnotationService.class);
			System.out.println(service);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Test
	public void testSay() {
		String result = service.say("abc");
		Assert.assertEquals("OK", result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSayWithIllegalArgumentException() {
		service.say("");
	}

	@Test(expected = NullPointerException.class)
	public void testSayWithNullPointerException() {
		service.say(null);
	}

}
