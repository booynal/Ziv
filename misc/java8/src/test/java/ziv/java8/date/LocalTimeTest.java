package ziv.java8.date;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import junit.framework.Assert;

import org.junit.Test;

import ziv.java8.BaseTest;

/**
 * <p>
 * 3. LocalTime 本地时间
 * </p>
 *
 * @author Booynal
 *
 */
public class LocalTimeTest extends BaseTest {

	/**
	 * LocalTime 定义了一个没有时区信息的时间，例如 晚上10点，或者 17:30:15。<br/>
	 * 下面的例子使用前面代码创建的时区创建了两个本地时间。之后比较时间并以小时和分钟为单位计算两个时间的时间差：
	 */
	@Test
	public void test() {
		ZoneId zone1 = ZoneId.of("Europe/Berlin");
		ZoneId zone2 = ZoneId.of("Brazil/East");

		LocalTime now1 = LocalTime.now(zone1);
		LocalTime now2 = LocalTime.now(zone2);

		System.out.println(now1.isBefore(now2)); // false
		Assert.assertFalse(now1.isBefore(now2));

		long hoursBetween = ChronoUnit.HOURS.between(now1, now2);
		System.out.println(hoursBetween); // -3
		Assert.assertEquals(-3, hoursBetween);

		long minutesBetween = ChronoUnit.MINUTES.between(now1, now2);
		System.out.println(minutesBetween); // -239
		Assert.assertEquals(-239, minutesBetween);
	}

	/**
	 * LocalTime 提供了多种工厂方法来简化对象的创建，包括解析时间字符串。
	 */
	@Test
	public void testFactory() {
		System.out.println("LocalTime 提供了多种工厂方法来简化对象的创建，包括解析时间字符串。");
		LocalTime late = LocalTime.of(23, 59, 59);
		System.out.println(late); // 23:59:59
		Assert.assertEquals("23:59:59", late.toString());

		DateTimeFormatter chinaFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).withLocale(Locale.CHINA);
		LocalTime leetTime1 = LocalTime.parse("13:37:22", chinaFormatter);
		System.out.println(leetTime1); // 13:37:22
		Assert.assertEquals("13:37:22", leetTime1.toString());

		DateTimeFormatter germanFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.GERMAN);
		LocalTime leetTime = LocalTime.parse("13:37", germanFormatter);
		System.out.println(leetTime); // 13:37
		Assert.assertEquals("13:37", leetTime.toString());
	}

}
