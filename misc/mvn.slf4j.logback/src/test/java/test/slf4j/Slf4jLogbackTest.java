package test.slf4j;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类描述:  对SLF4j的本地实现Logback的基本用法测试<br/>
 *
 * @auth 张黄江<br   />
 * @date 2018/8/24 10:55<br/>
 */
public class Slf4jLogbackTest {

	@Test
	public void test1() throws InterruptedException {
		Logger mylog = LoggerFactory.getLogger("mylog");
		mylog.trace("trace");
		mylog.debug("debug");
		mylog.info("info");
		mylog.warn("warn");
		mylog.error("error");
		for (int i = 0; i < 100000; i++) {
			Thread.sleep(1000);
			mylog.info("this is " + i);
		}
	}
}
