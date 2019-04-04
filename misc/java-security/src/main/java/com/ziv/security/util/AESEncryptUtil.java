package com.ziv.security.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 类描述:  AES 加密工具<br/>
 *
 * @auth 张黄江
 * @date 2019/3/28 16:47
 */
public class AESEncryptUtil {

	/** key生成器的算法 **/
	private static final String KEY_ALGORITHM = "AES";
	/** 默认的加密算法 **/
	private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	/** 生成密码对象的算法 **/
	private static final String SECURE_RANDOM_ALGORITHM = "SHA1PRNG";
	/** AES 要求密钥长度，以128位为下限，256比特为上限 **/
	private static final int KEY_SIZE = 128;
	/** 默认的字符集：UTF-8 **/
	private static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

	/**
	 * AES 加密操作
	 *
	 * @param content  待加密内容
	 * @param password 加密密码
	 * @return 返回Base64转码后的加密数据
	 */
	public static String encrypt(String content, String password) throws Exception {
		Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password));
		//通过Base64转码返回
		return Base64.getEncoder().encodeToString(cipher.doFinal(content.getBytes(CHARSET_UTF8)));
	}

	/**
	 * AES 解密操作
	 *
	 * @param content  待解密的字符串(Base64编码)
	 * @param password 解密密码
	 * @return 解密后的字符串
	 */
	public static String decrypt(String content, String password) throws Exception {
		Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));
		return new String(cipher.doFinal(Base64.getDecoder().decode(content)), CHARSET_UTF8);
	}

	/**
	 * 生成加密秘钥
	 *
	 * @return
	 */
	private static SecretKeySpec getSecretKey(final String password) throws NoSuchAlgorithmException {
		//返回生成指定算法密钥生成器的 KeyGenerator 对象
		KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
		SecureRandom secureRandom = SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM);
		secureRandom.setSeed(password.getBytes());
		keyGenerator.init(KEY_SIZE, secureRandom);
		//生成一个密钥
		SecretKey secretKey = keyGenerator.generateKey();
		// 转换为AES专用密钥
		return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
	}

}
