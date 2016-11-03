package mvn.spring.aop.aspect;

import org.springframework.stereotype.Component;

/**
 * @author Booynal
 *
 */
@Component
public class MyAspectAnnotationService {

	public String say(String message) {
		return innerSay(message);
	}

	private String innerSay(String message) {
		System.out.println(message);
		if (message.length() < 3) {
			throw new IllegalArgumentException("Too short message.");
		}
		return "OK";
	}

}
