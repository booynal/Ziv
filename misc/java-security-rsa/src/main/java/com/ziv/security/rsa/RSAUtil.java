package com.ziv.security.rsa;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA公钥/私钥/签名工具包<br/>
 * 字符串格式的密钥在未在特殊说明情况下都为BASE64编码格式<br/>
 *
 * @author 张黄江
 * @date 2019/3/26
 */
public class RSAUtil {

	/** 加密算法RSA */
	public static final String KEY_ALGORITHM = "RSA";
	/** 签名算法 */
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
	/** RSA算法的key长度 **/
	public static final int RSA_KEY_SIZE = 1024;

	/** RSA最大加密明文大小，否则会抛出：IllegalBlockSizeException: Data must not be longer than 117 bytes */
	private static final int MAX_ENCRYPT_BLOCK = 117;
	/** RSA最大解密密文大小，否则会抛出：IllegalBlockSizeException: Data must not be longer than 128 bytes */
	private static final int MAX_DECRYPT_BLOCK = 128;
	/** 默认的字符集：UTF-8 **/
	private static Charset charset = Charset.forName("UTF-8");

	/**
	 * 生成密钥对(公钥和私钥)
	 *
	 * @return 一对密钥，包含公钥和私钥
	 * @throws Exception
	 */
	public static RSAKeyPair generateKeyPair() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(RSA_KEY_SIZE);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		Base64.Encoder encoder = Base64.getEncoder();
		return new RSAKeyPair(encoder.encodeToString(keyPair.getPublic().getEncoded()), encoder.encodeToString(keyPair.getPrivate().getEncoded()));
	}

	/**
	 * 用私钥对信息生成数字签名
	 *
	 * @param data       已加密数据
	 * @param privateKey 私钥(BASE64编码)
	 * @return
	 * @throws Exception
	 */
	public static String sign(byte[] data, String privateKey) throws Exception {
		byte[] keyBytes = Base64.getDecoder().decode(privateKey.getBytes());
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(privateK);
		signature.update(data);
		return new String(Base64.getEncoder().encode(signature.sign()));
	}

	/**
	 * 校验数字签名
	 *
	 * @param data      已加密数据
	 * @param publicKey 公钥(BASE64编码)
	 * @param sign      数字签名
	 * @return
	 * @throws Exception
	 */
	public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
		byte[] keyBytes = Base64.getDecoder().decode(publicKey.getBytes());
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey publicK = keyFactory.generatePublic(keySpec);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(publicK);
		signature.update(data);
		return signature.verify(Base64.getDecoder().decode(sign.getBytes()));
	}

	/**
	 * 私钥解密(字符串版)
	 *
	 * @param encryptedString 已加密的字符串(BASE64编码)
	 * @param privateKey      私钥(BASE64编码)
	 * @return 解密后的字符串(BASE64编码)
	 * @throws Exception
	 */
	public static String decryptByPrivateKey(String encryptedString, String privateKey) throws Exception {
		return new String(decryptByPrivateKey(Base64.getDecoder().decode(encryptedString.getBytes(charset)), privateKey), charset);
	}

	/**
	 * 私钥解密(字节数组版)
	 *
	 * @param encryptedData 已加密的数据(字节数组)
	 * @param privateKey    私钥(BASE64编码)
	 * @return 解密后的数据(字节数组)
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
		byte[] keyBytes = Base64.getDecoder().decode(privateKey.getBytes());
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateK);
		// 对数据分段解密
		return getBytes(cipher, encryptedData, MAX_DECRYPT_BLOCK);
	}

	/**
	 * 公钥解密(字符串版)
	 *
	 * @param encryptedData 已加密的字符串(BASE64编码)
	 * @param publicKey     公钥(BASE64编码)
	 * @return 解密后的字符串
	 * @throws Exception
	 */
	public static String decryptByPublicKey(String encryptedData, String publicKey) throws Exception {
		return new String(decryptByPublicKey(Base64.getDecoder().decode(encryptedData), publicKey), charset);
	}

	/**
	 * 公钥解密(字节数组版)
	 *
	 * @param encryptedData 已加密的数据(字节数组)
	 * @param publicKey     公钥(BASE64编码)
	 * @return 解密后的数据(字节数组)
	 * @throws Exception
	 */
	public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
		byte[] keyBytes = Base64.getDecoder().decode(publicKey.getBytes());
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicK = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicK);
		// 对数据分段解密
		return getBytes(cipher, encryptedData, MAX_DECRYPT_BLOCK);
	}

	/**
	 * 公钥加密(字符串版)
	 *
	 * @param data      源数据(字符串)
	 * @param publicKey 公钥(BASE64编码)
	 * @return 加密后的密文(字符串)
	 * @throws Exception
	 */
	public static String encryptByPublicKey(String data, String publicKey) throws Exception {
		return Base64.getEncoder().encodeToString(encryptByPublicKey(data.getBytes(charset), publicKey));
	}

	/**
	 * 公钥加密(字节数组版)
	 *
	 * @param data      源数据(字节数组)
	 * @param publicKey 公钥(BASE64编码)
	 * @return 加密后的密文(字节数组)
	 * @throws Exception
	 */
	public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
		byte[] keyBytes = Base64.getDecoder().decode(publicKey.getBytes());
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicK = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicK);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		// 对数据分段加密
		return getBytes(cipher, data, MAX_ENCRYPT_BLOCK);
	}

	/**
	 * 私钥加密(字符串版)
	 *
	 * @param data       源数据(字符串)
	 * @param privateKey 私钥(BASE64编码)
	 * @return 加密的字符串(BASE64编码)
	 * @throws Exception
	 */
	public static String encryptByPrivateKey(String data, String privateKey) throws Exception {
		return Base64.getEncoder().encodeToString(encryptByPrivateKey(data.getBytes(charset), privateKey));
	}

	/**
	 * 私钥加密(字节数组版)
	 *
	 * @param data       源数据(字节数组)
	 * @param privateKey 私钥(BASE64编码)
	 * @return 加密的数据(字节数组)
	 * @throws Exception
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
		byte[] keyBytes = Base64.getDecoder().decode(privateKey.getBytes());
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateK);
		// 对数据分段加密
		return getBytes(cipher, data, MAX_ENCRYPT_BLOCK);
	}

	/** 使用Cipher对数据分段处理，根据maxLength分段 **/
	private static byte[] getBytes(Cipher cipher, byte[] data, int maxLength) throws IOException, IllegalBlockSizeException, BadPaddingException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		for (int remainder, thisLength, offset = 0; (remainder = data.length - offset) > 0; offset += thisLength) {
			out.write(cipher.doFinal(data, offset, thisLength = Math.min(remainder, maxLength)));
		}
		return out.toByteArray();
	}

	/** RSA的公钥私钥对 **/
	public static class RSAKeyPair {
		private String publicKey;
		private String privateKey;

		public RSAKeyPair(String publicKey, String privateKey) {
			this.publicKey = publicKey;
			this.privateKey = privateKey;
		}

		public String getPublicKey() {
			return publicKey;
		}

		public String getPrivateKey() {
			return privateKey;
		}

		@Override
		public String toString() {
			return "RSAKeyPair{" +
					"publicKey='" + publicKey + '\'' +
					", privateKey='" + privateKey + '\'' +
					'}';
		}
	}

}
