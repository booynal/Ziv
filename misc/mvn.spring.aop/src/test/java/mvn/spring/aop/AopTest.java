package mvn.spring.aop;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Booynal
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:mvn-spring-aop-test.xml")
public class AopTest {

	@Autowired
	private MyService service;

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
