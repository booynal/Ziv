package com.ziv.security.util;


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
import java.security.Signature;
import java.security.interfaces.RSAKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA公钥/私钥/签名工具包<br/>
 * 字符串格式的密钥在未在特殊说明情况下都为BASE64编码格式<br/>
 * <p>
 * <li>公钥加密->私钥解密</li>
 * <li>私钥加密->公钥解密</li>
 * <li>私钥签名->公钥验签</li>
 *
 * @author 张黄江
 * @date 2019/3/26
 */
public class RSAEncryptUtil {

	/** 加密算法RSA */
	public static final String KEY_ALGORITHM = "RSA";
	/** 签名算法 */
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
	/**
	 * RSA算法的key长度，最低值为512<br/>
	 * 长度为1024时候加密文本长度为172<br/>
	 * 长度为512时候加密文本长度为88<br/>
	 **/
	public static final int RSA_KEY_SIZE = 512;
	/** 默认的字符集：UTF-8 **/
	private static Charset charset = Charset.forName("UTF-8");

	/**
	 * 生成密钥对(公钥和私钥)
	 *
	 * @return 一对密钥，包含公钥和私钥
	 * @throws Exception
	 */
	public static RSAKeyPair generateKeyPair() throws Exception {
		return generateKeyPair(RSA_KEY_SIZE);
	}

	/**
	 * 生成密钥对(公钥和私钥)
	 *
	 * @param keySize 定义密钥的key的长度，最小为512，一般为2^N，如1024、2048等
	 * @return 一对密钥，包含公钥和私钥
	 * @throws Exception
	 */
	public static RSAKeyPair generateKeyPair(int keySize) throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(keySize);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		return new RSAKeyPair(encodeToString(keyPair.getPublic().getEncoded()), encodeToString(keyPair.getPrivate().getEncoded()));
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
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		byte[] bytes = privateKey.getBytes();
		signature.initSign(KeyFactory.getInstance(KEY_ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(decode(bytes))));
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
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(KeyFactory.getInstance(KEY_ALGORITHM).generatePublic(new X509EncodedKeySpec(decode(publicKey.getBytes()))));
		signature.update(data);
		return signature.verify(decode(sign.getBytes()));
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
		return new String(decryptByPrivateKey(decode(encryptedString.getBytes(charset)), privateKey), charset);
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
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateK = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decode(privateKey.getBytes())));
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateK);
		// 对数据分段解密
		return getBytes(cipher, encryptedData, getDecryptMaxLength((RSAKey) privateK));
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
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicK = keyFactory.generatePublic(new X509EncodedKeySpec(decode(publicKey.getBytes())));
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicK);
		// 对数据分段解密
		return getBytes(cipher, encryptedData, getDecryptMaxLength((RSAKey) publicK));
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
		return encodeToString(encryptByPublicKey(data.getBytes(charset), publicKey));
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
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicK = keyFactory.generatePublic(new X509EncodedKeySpec(decode(publicKey.getBytes())));
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicK);
		// 对数据分段加密
		return getBytes(cipher, data, getEncryptMaxLength((RSAKey) publicK));
	}

	/**
	 * 私钥加密(字符串版)
	 *
	 * @param string     源数据(字符串)
	 * @param privateKey 私钥(BASE64编码)
	 * @return 加密的字符串(BASE64编码)
	 * @throws Exception
	 */
	public static String encryptByPrivateKey(String string, String privateKey) throws Exception {
		return encodeToString(encryptByPrivateKey(string.getBytes(charset), privateKey));
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
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateK = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decode(privateKey.getBytes())));
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateK);
		// 对数据分段加密
		return getBytes(cipher, data, getEncryptMaxLength((RSAKey) privateK));
	}

	/** 将原始字节数组编码为BASE64编码的字符串 **/
	private static String encodeToString(byte[] src) {
		return Base64.getEncoder().encodeToString(src);
	}

	/** 将BASE64码解密为原始字节数组 **/
	private static byte[] decode(byte[] bytes) {
		return Base64.getDecoder().decode(bytes);
	}

	/**
	 * RSA最大加密明文长度，否则会抛出：IllegalBlockSizeException: Data must not be longer than 117 bytes<br/>
	 * 加密的明文最大长度=密钥的字节数-11<br/>
	 * 1024/8-11=128-11=117<br/>
	 * 512/8-11=64-11=53<br/>
	 * 为啥要减11字节呢？是因为RSA加密使用到了填充模式（padding），即内容不足117字节时会自动填满，用到填充模式自然会占用一定的字节，而且这部分字节也是参与加密的
	 */
	private static int getEncryptMaxLength(RSAKey rsaKey) {
		return rsaKey.getModulus().bitLength() / 8 - 11;
	}

	/** RSA最大解密密文长度，否则会抛出：IllegalBlockSizeException: Data must not be longer than 128 bytes */
	private static int getDecryptMaxLength(RSAKey rsaKey) {
		return rsaKey.getModulus().bitLength() / 8;
	}

	/** 使用Cipher对数据分段处理，根据maxLength分段 **/
	private static byte[] getBytes(Cipher cipher, byte[] data, int maxLength) throws IOException, IllegalBlockSizeException, BadPaddingException {
		if (data.length <= maxLength) {
			return cipher.doFinal(data, 0, data.length);
		} else {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			for (int remainder, thisLength, offset = 0; (remainder = data.length - offset) > 0; offset += thisLength) {
				out.write(cipher.doFinal(data, offset, thisLength = Math.min(remainder, maxLength)));
			}
			return out.toByteArray();
		}
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
