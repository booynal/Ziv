package com.ziv.security.rsa;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Base64;

@Ignore
public class RSAUtilTest {

	private static String source = "今天学习搜索引擎，明天学习NPL";

	private static String publicKey;
	private static String privateKey;

	@BeforeClass
	public static void setup() {
		try {
			RSAUtil.RSAKeyPair keyPair = RSAUtil.generateKeyPair();
			publicKey = keyPair.getPublicKey();
			privateKey = keyPair.getPrivateKey();
			System.out.println("公钥: \t" + publicKey);
			System.out.println("私钥：\t" + privateKey);
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test_公钥加密_私钥解密() throws Exception {
		System.out.println("测试点：公钥加密——私钥解密");
		System.out.println("原始文本：\t" + source);
		byte[] encodedData = RSAUtil.encryptByPublicKey(source.getBytes(), publicKey);
		System.out.println("加密后文本：\t" + new String(Base64.getEncoder().encode(encodedData)));
		byte[] decodedData = RSAUtil.decryptByPrivateKey(encodedData, privateKey);
		String decodedString = new String(decodedData);
		System.out.println("解密后文本：\t" + decodedString);
		Assert.assertEquals(source, decodedString);
	}

	@Test
	public void test_公钥加密_私钥解密_Base64编码的字符串() throws Exception {
		System.out.println("测试点：公钥加密——私钥解密（Base64编码的字符串）");
		System.out.println("原始文本：\t" + source);
		String encodedString = RSAUtil.encryptByPublicKey(source, publicKey);
		System.out.println("加密后文本：\t" + encodedString);
		String decodedString = RSAUtil.decryptByPrivateKey(encodedString, privateKey);
		System.out.println("解密后文本: \t" + decodedString);
		Assert.assertEquals(source, decodedString);
	}

	@Test
	public void test_私钥加密_公钥解密() throws Exception {
		System.out.println("测试点：私钥加密——公钥解密");
		System.out.println("原文字：\t" + source);
		byte[] data = source.getBytes();
		byte[] encodedData = RSAUtil.encryptByPrivateKey(data, privateKey);
		System.out.println("加密后：\t" + Base64.getEncoder().encodeToString(encodedData));
		byte[] decodedData = RSAUtil.decryptByPublicKey(encodedData, publicKey);
		String decodedString = new String(decodedData);
		System.out.println("解密后: \t" + decodedString);
		Assert.assertEquals(source, decodedString);
	}

	@Test
	public void test_私钥加密_公钥解密_Base64编码的字符串() throws Exception {
		System.out.println("测试点：私钥加密——公钥解密(Base64编码的字符串)");
		System.out.println("原文字：\t" + source);
		String encodedString = RSAUtil.encryptByPrivateKey(source, privateKey);
		System.out.println("加密后：\t" + encodedString);
		String decodedString = RSAUtil.decryptByPublicKey(encodedString, publicKey);
		System.out.println("解密后: \t" + decodedString);
		Assert.assertEquals(source, decodedString);
	}

	@Test
	public void testSign_私钥签名_公钥验证签名() throws Exception {
		System.out.println("私钥签名——公钥验证签名");
		byte[] data = source.getBytes();
		byte[] encodedData = RSAUtil.encryptByPrivateKey(data, privateKey);
		String sign = RSAUtil.sign(encodedData, privateKey);
		System.out.println("签名：\t" + sign);
		boolean status = RSAUtil.verify(encodedData, publicKey, sign);
		System.out.println("验证结果：\t" + status);
		Assert.assertTrue(status);
	}

	@Test
	public void testHttpSign() throws Exception {
		String param = "id=1&name=张三";
		System.out.println("原文：\t" + param);
		String encodedString = RSAUtil.encryptByPrivateKey(param, privateKey);
		System.out.println("加密后：\t" + encodedString);

		String decodedString = RSAUtil.decryptByPublicKey(encodedString, publicKey);
		System.out.println("解密后：\t" + decodedString);
		Assert.assertEquals(param, decodedString);

		String sign = RSAUtil.sign(encodedString.getBytes(), privateKey);
		System.out.println("签名：\t" + sign);

		boolean status = RSAUtil.verify(encodedString.getBytes(), publicKey, sign);
		System.out.println("签名验证结果：\t" + status);
		Assert.assertTrue(status);
	}

}
