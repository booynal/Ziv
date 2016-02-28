package ziv.java8.date;

import java.time.ZoneId;

import junit.framework.Assert;

import org.junit.Test;

import ziv.java8.BaseTest;

/**
 * <p>
 * 2. Timezones 时区
 * </p>
 * 在新API中时区使用ZoneId来表示。时区可以很方便的使用静态方法of来获取到。<br/>
 * * 时区定义了到UTS时间的时间差，在Instant时间点对象到本地日期对象之间转换的时候是极其重要的。
 *
 * @author Booynal
 *
 */
public class TimezonesTest extends BaseTest {

	@Test
	public void test() {
		// prints all available timezone ids
		System.out.println(ZoneId.getAvailableZoneIds());
		ZoneId zone1 = ZoneId.of("Europe/Berlin");
		ZoneId zone2 = ZoneId.of("Brazil/East");
		System.out.println(zone1.getRules()); // ZoneRules[currentStandardOffset=+01:00]
		System.out.println(zone2.getRules()); // ZoneRules[currentStandardOffset=-03:00]

		// 上海时区
		ZoneId shanghaiZone = ZoneId.of("Asia/Shanghai");
		System.out.println(shanghaiZone.getRules()); // ZoneRules[currentStandardOffset=+08:00]
		Assert.assertEquals("ZoneRules[currentStandardOffset=+08:00]", shanghaiZone.getRules().toString());

		// 重庆时区
		ZoneId chongqingZone = ZoneId.of("Asia/Chongqing");
		System.out.println(chongqingZone.getRules()); // ZoneRules[currentStandardOffset=+08:00]
		Assert.assertEquals("ZoneRules[currentStandardOffset=+08:00]", chongqingZone.getRules().toString());
	}
}
