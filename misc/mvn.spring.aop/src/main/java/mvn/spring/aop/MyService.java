package mvn.spring.aop;

/**
 * @author Booynal
 *
 */
public class MyService {

	public String say(String message) {
		System.out.println(message);
		if (message.length() < 3) {
			throw new IllegalArgumentException("Too short message.");
		}
		return "OK";
	}

}
