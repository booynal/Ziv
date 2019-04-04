package com.ziv.security.util;

import org.junit.Test;

import javax.crypto.BadPaddingException;

/**
 * 类描述:  对AES加密工具类的测试<br/>
 *
 * @auth 张黄江
 * @date 2019/3/28 17:34
 */
public class AESEncryptUtilTest {

	@Test
	public void test() {
		String s = "hello,您好！欢迎来到加密的安全世界";
		System.out.println("原文: \t" + s.length() + "\t" + s);

		try {
			String s1 = AESEncryptUtil.encrypt(s, "1234");
			System.out.println("密文: \t" + s1.length() + "\t" + s1);

			String s2 = AESEncryptUtil.decrypt(s1, "1234");
			System.out.println("解密: \t" + s2.length() + "\t" + s2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test2() throws Exception {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 50; i++) {
			sb.append(i);
			String encrypt = AESEncryptUtil.encrypt(sb.toString(), "abc");
			System.out.println(encrypt.length() + "\t" + encrypt);
		}
	}

	@Test
	public void test2_加密中文() throws Exception {
		String string = "鸭肉性质平和性平，滋味甜中带咸，有滋阴养胃的功效。而且在药书里面对鸭肉也有记载，认为常吃鸭肉可以滋补";
		System.out.println("string: \t" + string.length() + "\t" + string);
		String encrypt = AESEncryptUtil.encrypt(string, "abc");
		System.out.println("encrypt: \t" + encrypt.length() + "\t" + encrypt);
	}

	@Test
	public void test3_测试对空串加密() throws Exception {
		String encrypt = AESEncryptUtil.encrypt("", "abc");
		System.out.println("encrypt: \t" + encrypt.length() + "\t" + encrypt);
	}

	@Test(expected = BadPaddingException.class)
	public void test4_测试用不同的密码解密() throws Exception {
		String encrypt = AESEncryptUtil.encrypt("name", "abc");
		System.out.println("encrypt: \t" + encrypt.length() + "\t" + encrypt);

		// 报错: javax.crypto.BadPaddingException: Given final block not properly padded
		String decrypt = AESEncryptUtil.decrypt(encrypt, "1234"); // 用不同的密码解密会抛异常
		System.out.println("decrypt: \t" + decrypt.length() + "\t" + decrypt);
	}

	@Test(expected = BadPaddingException.class)
	public void test5_测试解密一个明文() throws Exception {
		// 会得到密码不对的错误: javax.crypto.BadPaddingException: Given final block not properly padded
		String decrypt = AESEncryptUtil.decrypt("QSM88VlUOUGKDm9WYpT10g==", "1234");
		System.out.println("decrypt: \t" + decrypt.length() + "\t" + decrypt);
	}
}
