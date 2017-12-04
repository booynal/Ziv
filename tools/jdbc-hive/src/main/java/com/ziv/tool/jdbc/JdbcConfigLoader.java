/**
 * JdbcConfigLoader.java
 */
package com.ziv.tool.jdbc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author ziv
 * @date 2017年12月4日 上午11:10:29
 */
public class JdbcConfigLoader {

	private static final String configFileName = "jdbc.hive.properties";

	public static JdbcConfig loadConfig() {
		InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(configFileName);
		Properties properties = new Properties();
		if (null != resourceAsStream) {
			try {
				properties.load(resourceAsStream);
			} catch (IOException e) {
				System.err.println("加载缺省配置文件出错:");
				e.printStackTrace();
			}
		}
		File file = new File(configFileName);
		if (file.exists()) {
			try {
				properties.load(new FileInputStream(file));
			} catch (Exception e) {
				System.err.println(String.format("加载配置文件'%s'出错:", file.getAbsolutePath()));
				e.printStackTrace();
			}
		}

		JdbcConfig config = new JdbcConfig();
		config.setDriverName(properties.getProperty("jdbc.driver_name"));
		config.setUrl(properties.getProperty("jdbc.url"));
		config.setUser(properties.getProperty("jdbc.user"));
		config.setPassword(properties.getProperty("jdbc.password"));
		System.out.println(String.format("加载到的配置信息: '%s'", config));
		return config;
	}

}
