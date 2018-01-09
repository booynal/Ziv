/**
 * NumberBaseUtil.java
 */
package com.ziv.number.base;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ziv
 * @date 2018年1月8日 下午10:45:51
 */
public class NumberBaseUtil {

	private static final char[] CHARS_62 = new char[62];

	static {
		int chars62Index = 0;
		for (int i = '0'; i <= '9'; i++) {
			CHARS_62[chars62Index++] = (char) i;
		}
		for (int i = 'A'; i <= 'Z'; i++) {
			CHARS_62[chars62Index++] = (char) i;
		}
		for (int i = 'a'; i <= 'z'; i++) {
			CHARS_62[chars62Index++] = (char) i;
		}
		System.err.println("CHARS_62: " + Arrays.toString(CHARS_62));
	}

	/** 从10进制转换为N进制 **/
	public static String convertDecimalToN(final int decimalNumber, final int N) {
		List<Integer> indices = convertDecimalToNIndex(decimalNumber, N);
		if (null != indices && indices.isEmpty() == false) {
			StringBuilder sb = new StringBuilder();
			for (Integer integer : indices) {
				sb.append(CHARS_62[integer]);
			}
			return sb.toString();
		}
		return null;
	}

	/** 从10进制转换为N进制 **/
	public static List<Integer> convertDecimalToNIndex(final int decimalNumber, final int N) {
		LinkedList<Integer> list = new LinkedList<>();
		int tmp = decimalNumber;
		while (tmp >= N) {
			list.addFirst(tmp % N);
			tmp /= N;
		}
		list.addFirst(tmp);
		return list;
	}

	/** 从N进制转换为10进制 **/
	public static int convertNToDecimal(final int N, String number) {
		char[] charArray = number.toCharArray();
		int sum = 0;
		for (int pow = 0, index = charArray.length - 1; index >= 0; index--, pow++) {
			sum += findIndex(charArray[index]) * (int) Math.pow(N, pow);
		}
		return sum;
	}

	private static int findIndex(char c) {
		if (c >= '0' && c <= '9') {
			return c - '0';
		} else if (c >= 'A' && c <= 'Z') {
			return c - 'A' + 10;
		} else if (c >= 'a' && c <= 'z') {
			return c - 'a' + 36;
		} else {
			throw new RuntimeException("not supported char: " + c);
		}
	}

	/** N进制转换为M进制 **/
	public static String convertNToM(int N, final String numberFrom, int M) {
		return convertDecimalToN(convertNToDecimal(N, numberFrom), M);
	}

}
