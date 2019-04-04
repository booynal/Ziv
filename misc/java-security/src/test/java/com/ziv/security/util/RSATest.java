package com.ziv.security.util;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * 类描述:  RSA算法的测试<br/>
 *
 * @auth 张黄江
 * @date 2019/3/26 19:16
 */
public class RSATest {

	/** RSA最大加密明文大小 */
	private static final int MAX_ENCRYPT_BLOCK = 117;
	/** RSA最大解密密文大小 */
	private static final int MAX_DECRYPT_BLOCK = 128;

	public static String publicKeyString;
	public static String privateKeyString;

	@BeforeClass
	public static void setup() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		publicKeyString = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
		privateKeyString = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
		System.out.println("publicKeyString: " + publicKeyString);
		System.out.println("privateKeyString: " + privateKeyString);
	}

	public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
		byte[] keyBytes = Base64.getDecoder().decode(publicKey.getBytes());
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		Key publicK = keyFactory.generatePublic(x509KeySpec);
		// 对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicK);
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return encryptedData;
	}

	public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
		byte[] keyBytes = Base64.getDecoder().decode(privateKey.getBytes());
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateK);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return decryptedData;
	}

	@Test
	public void test_encryptByPublicKey() throws Exception {
		System.out.println("测试点：通过公钥加密");
		String source = "ziv";
//		String source = "DtaX46UIz6FsNw4hj/vSH9cWp0CG+V7itxxk6n2H8n10Z1C97Tnic+9EqTLIbJPXiJFTTRYO+B8WYQbio6/xkvgtwi89V5lVKvHKm+5B2afK+SAJmfzRC0RERmzooPbyjR9eNUiAiRvpP2u/2k06e7UBVfoSiV+Z193mVi607KE=";
		byte[] data = source.getBytes("UTF-8");
		byte[] keyBytes = Base64.getDecoder().decode(publicKeyString.getBytes());

		System.out.println("source: " + source);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		Key publicK = keyFactory.generatePublic(x509KeySpec);
		// 对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicK);
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		// 对数据分段加密
		for (int rem, len, offset = 0; (rem = inputLen - offset) > 0; offset += len) {
			len = Math.min(rem, MAX_ENCRYPT_BLOCK);
			out.write(cipher.doFinal(data, offset, len));
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		System.out.println("encodeToString: " + Base64.getEncoder().encodeToString(encryptedData));
		byte[] bytes = encryptByPublicKey(data, publicKeyString);
		System.out.println("encodeToString2: " + Base64.getEncoder().encodeToString(bytes));
		byte[] decryptByPrivateKey = decryptByPrivateKey(encryptedData, privateKeyString);
		System.out.println("decryptByPrivateKey: " + new String(decryptByPrivateKey));
	}

}
