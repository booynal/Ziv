package com.ziv.security.util;

import org.junit.Test;

/**
 * 类描述:  按位加密工具测试类<br/>
 *
 * @auth 张黄江
 * @date 2019/3/27 17:49
 */
public class BitXorEncryptUtilTest {

	@Test
	public void encrypt() {
		System.out.println("基于按位异或的方式加密解密测试：");
		String test = "18911069875";
		String key = "pf_huayong";

		System.out.println("原文:\t" + test.length() + "\t" + test);
		String testEncrypt = BitXorEncryptUtil.encrypt(test, key);
		System.out.println("密文:\t" + testEncrypt.length() + "\t" + testEncrypt);
		System.out.println("将密文再次加密: " + BitXorEncryptUtil.encrypt(testEncrypt, key));
		String testDecrypt = BitXorEncryptUtil.decrypt(testEncrypt, key);
		System.out.println("解密:\t" + testDecrypt.length() + "\t" + testDecrypt);
	}
}
