package com.ziv.security.util;

import org.junit.Test;

import java.util.Base64;

/**
 * 类描述:  Base64测试类<br/>
 *
 * @auth 张黄江
 * @date 2019/3/26 19:29
 */
public class Base64Test {

	private String source = "ziv";

	@Test
	public void test() {
		System.out.println("source: " + source);
		byte[] src = source.getBytes();
		String encode = Base64.getEncoder().encodeToString(src);
		System.out.println("encode: " + encode);
		byte[] decode = Base64.getDecoder().decode(encode);
		System.out.println("decode: " + new String(decode));
	}

}
