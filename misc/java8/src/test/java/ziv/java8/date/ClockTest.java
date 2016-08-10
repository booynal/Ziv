package ziv.java8.date;

import java.time.Clock;
import java.time.Instant;
import java.util.Date;

import org.junit.Test;

import test.junit.base.BaseTest;

/**
 * <p>
 * 1. Clock 时钟
 * </p>
 * Clock类提供了访问当前日期和时间的方法，Clock是时区敏感的，可以用来取代 System.currentTimeMillis()来获取当前的微秒数。<br/>
 * 某一个特定的时间点也可以使用Instant类来表示，Instant类也可以用来创建老的java.util.Date对象。
 *
 * @author Booynal
 *
 */
public class ClockTest extends BaseTest {

	@Test
	public void test() {
		Clock clock = Clock.systemDefaultZone();
		long millis = clock.millis();
		Instant instant = clock.instant();
		Date legacyDate = Date.from(instant); // legacy java.util.Date

		System.out.println(millis);
		System.out.println(instant);
		System.out.println(legacyDate);
	}
}
