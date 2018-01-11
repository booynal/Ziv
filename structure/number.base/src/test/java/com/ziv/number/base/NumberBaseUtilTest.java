/**
 * NumberBaseUtil.java
 */
package com.ziv.number.base;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author ziv
 * @date 2018年1月9日 上午10:26:12
 */
public class NumberBaseUtilTest {

	@Test
	public void test() {
		System.out.println(NumberBaseUtil.convertDecimalToNIndex(6, 2));
		System.out.println(NumberBaseUtil.convertDecimalToNIndex(66, 62));

		String string = NumberBaseUtil.convertDecimalToN(6, 2);
		System.out.println(string = NumberBaseUtil.convertDecimalToN(6, 2));
		Assert.assertEquals("110", string);
		System.out.println(string = NumberBaseUtil.convertDecimalToN(17, 62));
		Assert.assertEquals("H", string);
		System.out.println(string = NumberBaseUtil.convertDecimalToN(66, 62));
		Assert.assertEquals("14", string);
		System.out.println(string = NumberBaseUtil.convertDecimalToN(166, 62));
		Assert.assertEquals("2g", string);

		long decimal = NumberBaseUtil.convertNToDecimal(2, "001");
		System.out.println(decimal);
		Assert.assertEquals(1, decimal);
		System.out.println(decimal = NumberBaseUtil.convertNToDecimal(2, "011"));
		Assert.assertEquals(3, decimal);
		System.out.println(decimal = NumberBaseUtil.convertNToDecimal(2, "100"));
		Assert.assertEquals(4, decimal);
		System.out.println(decimal = NumberBaseUtil.convertNToDecimal(2, "101"));
		Assert.assertEquals(5, decimal);
		System.out.println(decimal = NumberBaseUtil.convertNToDecimal(2, "111"));
		Assert.assertEquals(7, decimal);

		System.out.println(decimal = NumberBaseUtil.convertNToDecimal(3, "1"));
		Assert.assertEquals(1, decimal);
		System.out.println(decimal = NumberBaseUtil.convertNToDecimal(3, "10"));
		Assert.assertEquals(3, decimal);
		System.out.println(decimal = NumberBaseUtil.convertNToDecimal(3, "11"));
		Assert.assertEquals(4, decimal);

		System.out.println(decimal = NumberBaseUtil.convertNToDecimal(16, "10"));
		Assert.assertEquals(16, decimal);
		System.out.println(decimal = NumberBaseUtil.convertNToDecimal(16, "A"));
		Assert.assertEquals(10, decimal);
		System.out.println(decimal = NumberBaseUtil.convertNToDecimal(16, "11"));
		Assert.assertEquals(17, decimal);
		System.out.println(decimal = NumberBaseUtil.convertNToDecimal(16, "1A"));
		Assert.assertEquals(26, decimal);

		System.out.println(decimal = NumberBaseUtil.convertNToDecimal(62, "A"));
		Assert.assertEquals(10, decimal);
		System.out.println(decimal = NumberBaseUtil.convertNToDecimal(62, "Z"));
		Assert.assertEquals(35, decimal);
		System.out.println(decimal = NumberBaseUtil.convertNToDecimal(62, "a"));
		Assert.assertEquals(36, decimal);
		System.out.println(decimal = NumberBaseUtil.convertNToDecimal(62, "z"));
		Assert.assertEquals(61, decimal);
		System.out.println(decimal = NumberBaseUtil.convertNToDecimal(62, "10"));
		Assert.assertEquals(62, decimal);
		System.out.println(decimal = NumberBaseUtil.convertNToDecimal(62, "11"));
		Assert.assertEquals(63, decimal);
		System.out.println(decimal = NumberBaseUtil.convertNToDecimal(62, "21"));
		Assert.assertEquals(125, decimal);
		System.out.println(decimal = NumberBaseUtil.convertNToDecimal(62, "Aa"));
		Assert.assertEquals(656, decimal);
	}

	@Test
	public void test_taobao_short_url() {
		// 短URL为：https://s.click.taobao.com/i8DE6Vw
		// 长URL为：https://item.taobao.com/item.htm?id=546715479554&ali_trackid=2:mm_125292550_38616603_143002673:1515634260_324_281125427&pvid=10_61.148.52.74_515_1515633415397
		// i8DE6Vw -> 2506734882356
		long decimal;
		System.out.println(decimal = NumberBaseUtil.convertNToDecimal(62, "i8DE6Vw"));
		Assert.assertEquals(2506734882356L, decimal);
	}

	@Test
	public void test_n_to_m() {
		String convert = null;
		System.out.println(convert = NumberBaseUtil.convertNToM(2, "110", 10));
		Assert.assertEquals("6", convert);
		System.out.println(convert = NumberBaseUtil.convertNToM(10, "5", 2));
		Assert.assertEquals("101", convert);
		System.out.println(convert = NumberBaseUtil.convertNToM(2, "101", 3));
		Assert.assertEquals("12", convert);
		System.out.println(convert = NumberBaseUtil.convertNToM(2, "1010", 16));
		Assert.assertEquals("A", convert);
		System.out.println(convert = NumberBaseUtil.convertNToM(10, "70", 62));
		Assert.assertEquals("18", convert);
		System.out.println(convert = NumberBaseUtil.convertNToM(10, "343272370", 62));
		Assert.assertEquals("NEKp8", convert);
		System.out.println(convert = NumberBaseUtil.convertNToM(62, "scapp", 10));
		Assert.assertEquals("1002361628", convert);
		System.out.println(convert = NumberBaseUtil.convertNToM(62, "ziv", 10));
		Assert.assertEquals("288060", convert);
		System.out.println(convert = NumberBaseUtil.convertNToM(62, "ivy", 10));
		Assert.assertEquals("223521", convert);
	}

}
