package mvn.spring.helloworld.subpackage;

import org.springframework.stereotype.Component;

/**
 * 测试Spring是否能够扫描子包
 *
 * @author ziv
 *
 */
@Component
public class SubPackageService {

	public void say(String user) {
		System.out.println(user + " 调用了子包服务");
	}

}
