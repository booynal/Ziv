/**
 * Application.java
 */
package test.spring.boot;

import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ziv
 * @date 2017年2月21日 上午10:58:45
 */
@RestController
@EnableAutoConfiguration
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@RequestMapping("/")
	public String home() {
		return "Hello world!" + System.lineSeparator();
	}

	@RequestMapping("/now")
	public String now() {
		return "now: " + new Date() + System.lineSeparator();
	}

	@RequestMapping("/say/{message}")
	public String say(@PathVariable String message) {
		System.out.println(message);
		return message + System.lineSeparator();
	}
}
