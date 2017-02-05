package ziv.java8.date;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

import test.junit.base.BaseTest;

/**
 * <p>
 * 4. LocalDate 本地日期
 * </p>
 * LocalDate 表示了一个确切的日期，比如 2014-03-11。该对象值是不可变的，用起来和LocalTime基本一致。
 *
 * @author Booynal
 *
 */
public class LocalDateTest extends BaseTest {

	/**
	 * 下面的例子展示了如何给Date对象加减天/月/年。另外要注意的是这些对象是不可变的，操作返回的总是一个新实例。
	 */
	@Test
	public void test() {
		LocalDate today = LocalDate.now();
		System.out.println("today is: " + today);

		LocalDate tomorrow = today.plus(1, ChronoUnit.DAYS);
		System.out.println("tomorrow is: " + tomorrow);

		LocalDate yesterday = tomorrow.minusDays(2);
		System.out.println("yesterday is: " + yesterday);

		LocalDate independenceDay = LocalDate.of(2014, Month.JULY, 4);
		DayOfWeek dayOfWeek = independenceDay.getDayOfWeek();
		System.out.println(dayOfWeek);
		Assert.assertEquals("FRIDAY", dayOfWeek.name());
	}

	/**
	 * 从字符串解析一个LocalDate类型和解析LocalTime一样简单：
	 */
	@Test
	public void testParse() {
		DateTimeFormatter chinaFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.CHINA);
		LocalDate parse = LocalDate.parse("2014-12-24", chinaFormatter);
		System.out.println(parse); // 2014-12-24
		Assert.assertEquals(2014, parse.getYear());
		Assert.assertEquals(12, parse.getMonthValue());
		Assert.assertEquals(24, parse.getDayOfMonth());

		DateTimeFormatter germanFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.GERMAN);
		LocalDate xmas = LocalDate.parse("24.12.2014", germanFormatter);
		System.out.println(xmas); // 2014-12-24
		Assert.assertEquals("2014-12-24", xmas.toString());
	}

}
