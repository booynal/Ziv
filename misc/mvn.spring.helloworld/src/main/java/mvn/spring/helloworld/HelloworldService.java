package mvn.spring.helloworld;

import org.springframework.stereotype.Component;

@Component
public class HelloworldService {

	public String sayHello(String user) {
		System.out.println(user);
		return "ok";
	}

}
