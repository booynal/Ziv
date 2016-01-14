package mvn.spring.aop.annotation;

/**
 * @author Booynal
 *
 */
public class MyAnnotationService {

	@EnableLogException
	public String say(String message) {
		System.out.println(message);
		if (message.length() < 3) {
			throw new IllegalArgumentException("Too short message.");
		}
		return "OK";
	}

}
