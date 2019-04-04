package com.ziv.security.util;

/**
 * 类描述:  按位加密/解密工具<br/>
 * 缺点：连续两次加密就等于解密
 *
 * @auth 张黄江
 * @date 2019/3/27 17:48
 */
public class BitXorEncryptUtil {

	/** 默认的密码 **/
	private static final String DEFAULT_KEY = "DEFAULT_KEY";

	/**
	 * 方法描述: 加密文本，使用默认的密码<br/>
	 *
	 * @param clearText 明文字符串
	 * @return 加密后的字符串
	 * @author 张黄江
	 * @date 2019/3/27 17:51
	 */
	public static String encrypt(String clearText) {
		return encrypt(clearText, DEFAULT_KEY);
	}

	/**
	 * 方法描述: 加密文本，使用自定义的密码<br/>
	 *
	 * @param clearText 明文字符串
	 * @param key       密码字符串
	 * @return 加密后的字符串
	 * @author 张黄江
	 * @date 2019/3/27 17:51
	 */
	public static String encrypt(String clearText, String key) {
		return getString(clearText, key);
	}

	/**
	 * 方法描述: 解密字符串，使用默认的密码<br/>
	 *
	 * @param cipherText 密文字符串
	 * @return 解密后的字符串
	 * @author 张黄江
	 * @date 2019/3/27 17:52
	 */
	public static String decrypt(String cipherText) {
		return decrypt(cipherText, DEFAULT_KEY);
	}

	/**
	 * 方法描述: 解密字符串，使用自定义的密码<br/>
	 *
	 * @param cipherText 密文字符串
	 * @param key        密码字符串
	 * @return 解密后的字符串
	 * @author 张黄江
	 * @date 2019/3/27 17:52
	 */
	public static String decrypt(String cipherText, String key) {
		return getString(cipherText, key);
	}

	/** 按位异或的方式处理字符串 **/
	private static String getString(String string, String key) {
		int keyLength = key.length();
		char[] array = string.toCharArray();
		char[] keyArray = key.toCharArray();
		for (int i = 0; i < array.length; i++) {
			array[i] = (char) (array[i] ^ keyArray[i % keyLength]);
		}
		return new String(array);
	}

}
