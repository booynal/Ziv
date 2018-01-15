/**
 * Application.java
 */
package test.spring.boot;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ziv
 * @date 2017年2月21日 上午10:58:45
 */
@RestController
@EnableAutoConfiguration
@ComponentScan("test.spring.boot")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/** 访问根目录 */
	@RequestMapping("/")
	public String home() {
		return "Hello world!" + System.lineSeparator();
	}

	/** 访问指定路径 **/
	@RequestMapping("/now")
	public String now() {
		return "now: " + new Date() + System.lineSeparator();
	}

	/** 将路径映射为参数 **/
	@RequestMapping("/say/{message}")
	public String say(@PathVariable String message) {
		System.out.println(message);
		return message + System.lineSeparator();
	}

	/** 获取Http请求的引用映射到参数上，同时获取Header中的值映射到参数上，同时获取url中的值映射到参数上 */
	@RequestMapping("/{shortUrl}")
	public String shortUrl(HttpServletRequest request, @RequestHeader("host") String hostAndPort, @PathVariable String shortUrl) {
		System.out.println("shortUrl: " + shortUrl);
		String protocol = request.getProtocol().split("/")[0].toLowerCase();
		String contextPath = null == request.getContextPath() ? "" : request.getContextPath(); // 格式为: '/project'
		return String.format("%s://%s%s/%s", protocol, hostAndPort, contextPath, shortUrl.hashCode());
	}

	/** 获取请求相关信息 **/
	@RequestMapping("/getInfo")
	public String getRequestInfo(HttpServletRequest request) {
		System.out.println(request.getRequestedSessionId());
		System.out.println("contextPath: " + request.getContextPath()); // 项目名
		System.out.println("servletPath: " + request.getServletPath()); // 接口名
		System.out.println("URI: " + request.getRequestURI()); // 获取端口后面的路径，本例中返回: '/getInfo'
		System.out.println("URL: " + request.getRequestURL()); // 获取访问本方法的整个路径，包括域名部分，本例中返回: 'http://localhost:8080/getInfo'
		System.out.println("serverName: " + request.getServerName()); // 获取服务器名称，即ip或域名，本例返回: 'localhost'
		System.out.println("protocol: " + request.getProtocol().split("/")[0]); // 返回协议，本例返回: 'HTTP/1.1'，所以需要按照/拆分
		Enumeration<String> headerNames = request.getHeaderNames();
		if (null != headerNames) {
			while (headerNames.hasMoreElements()) {
				String name = headerNames.nextElement();
				System.out.println(String.format("header-'%s'\t= '%s'", name, request.getHeader(name)));
			}
		}
		String format = String.format("remoteAddr: '%s', remoteHost: '%s', remotePort: '%s', remoteUser: '%s'", request.getRemoteAddr(), request.getRemoteHost(), request.getRemotePort(), request.getRemoteUser());
		System.out.println(format);
		return format;
	}

	/** 获取post的数据，通过@RequestBody标注post请求的数据映射到参数上 **/
	@PostMapping("/post")
	public String post(HttpServletRequest request, @RequestBody String body) {
		try {
			System.out.println("body: " + URLDecoder.decode(body, request.getCharacterEncoding())); // 经过转码，还原出原始的字符串
		} catch (IOException e) {
			e.printStackTrace();
		}

		// POST的数据也可以通过参数Map获取，但是这种方式会将字符串按照=拆分
		Map<String, String[]> parameterMap = request.getParameterMap();
		if (null != parameterMap) {
			for (Entry<String, String[]> entry : parameterMap.entrySet()) {
				System.out.println(entry.getKey() + "\t= " + Arrays.toString(entry.getValue()));
			}
		}
		return "post";
	}
}
