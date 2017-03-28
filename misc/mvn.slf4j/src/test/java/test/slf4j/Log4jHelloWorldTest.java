package test.slf4j;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SLF4J的结构：<br/>
 * 需要一个slf4j-api.jar还需要转换为底层log实现的adapter，最后是底层的log实现。一共需要3种jar包<br/>
 * 如果在pom.xml种定义了多个底层实现，则会打印警告信息，并优先选择第一次出现的底层实现
 * Created by ziv on 2017/3/28.
 */
public class Log4jHelloWorldTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(Log4jHelloWorldTest.class);

	/**
	 * 测试点：测试SLF4J对log4j的使用<br/>
	 * 测试方法：修改pom.xml，注释掉ch.qos.logback:logback-classic，保留org.slf4j:slf4j-log4j12
	 */
	@Test
	public void test_log4j() {
		LOGGER.info("Hello {}", "log4j");
	}

	/**
	 * 测试点：测试SLF4J对logbook的使用<br/>
	 * 测试方法：修改pom.xml，注释掉org.slf4j:slf4j-log4j12，保留ch.qos.logback:logback-classic
	 */
	@Test
	public void test_logback() {
		LOGGER.info("Hello {}", "logback");
	}
}
