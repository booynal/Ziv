package ziv.java8.date;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import test.junit.base.BaseTest;

/**
 * <p>
 * LocalDateTime 本地日期时间
 * </p>
 * LocalDateTime 同时表示了时间和日期，相当于前两节内容合并到一个对象上了。<br/>
 * LocalDateTime和LocalTime还有LocalDate一样，都是不可变的。
 *
 * @author Booynal
 *
 */
public class LocalDateTimeTest extends BaseTest {

	@Test
	public void test() {
		LocalDateTime sylvester = LocalDateTime.of(2014, Month.DECEMBER, 31, 23, 59, 59);
		DayOfWeek dayOfWeek = sylvester.getDayOfWeek();
		System.out.println(dayOfWeek); // WEDNESDAY
		Assert.assertEquals("WEDNESDAY", dayOfWeek.name());

		Month month = sylvester.getMonth();
		System.out.println(month); // DECEMBER
		Assert.assertEquals("DECEMBER", month.name());

		long minuteOfDay = sylvester.getLong(ChronoField.MINUTE_OF_DAY);
		System.out.println(minuteOfDay); // 1439
		Assert.assertEquals(1439, minuteOfDay);
	}

	/**
	 * 只要附加上时区信息，就可以将其转换为一个时间点Instant对象，Instant时间点对象可以很容易的转换为老式的java.util.Date。
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testZone() {
		LocalDateTime sylvester = LocalDateTime.of(2014, Month.DECEMBER, 31, 23, 59, 59);

		Instant instant = sylvester.atZone(ZoneId.of("Asia/Shanghai")).toInstant();
		Date legacyDate = Date.from(instant);
		System.out.println(legacyDate); // Wed Dec 31 23:59:59 CET 2014
		Assert.assertEquals(2014, legacyDate.getYear() + 1900);
		Assert.assertEquals(11, legacyDate.getMonth());
		Assert.assertEquals(31, legacyDate.getDate());
		Assert.assertEquals(23, legacyDate.getHours());
		Assert.assertEquals(59, legacyDate.getMinutes());
		Assert.assertEquals(59, legacyDate.getSeconds());
	}

	/**
	 * 格式化LocalDateTime和格式化时间和日期一样的，除了使用预定义好的格式外，我们也可以自己定义格式：
	 */
	@Test
	public void testFormat() {
		// 和java.text.NumberFormat不一样的是新版的DateTimeFormatter是不可变的，所以它是线程安全的。
		// 关于时间日期格式的详细信息：http://download.java.net/jdk8/docs/api/java/time/format/DateTimeFormatter.html
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime parsed = LocalDateTime.parse("2016-02-28 15:58:39", formatter);
		String string = formatter.format(parsed);
		System.out.println(string); // 2016-02-28 15:58:39

		Assert.assertEquals(2016, parsed.getYear());
		Assert.assertEquals(2, parsed.getMonthValue());
		Assert.assertEquals(28, parsed.getDayOfMonth());
		Assert.assertEquals(15, parsed.getHour());
		Assert.assertEquals(58, parsed.getMinute());
		Assert.assertEquals(39, parsed.getSecond());
	}

}
