/**
 * OdpsMain.java
 */
package com.ziv.tool.jdbc.odps;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

/**
 * @author ziv
 * @date 2017年12月1日 上午10:05:50
 */
public class OdpsMain {

	public static void main(String[] args) {
		System.setOut(LogStreamWarpper.warp(System.out));
		System.setErr(LogStreamWarpper.warp(System.err));
		loadConfig();

		if (args.length <= 0) {
			System.err.println("Usage: " + OdpsMain.class.getName() + " sql");
			System.exit(-1);
		}
		String sql = StringUtils.join(args, ' ');
		System.out.println(String.format("sql: '%s'", sql));
		try {
			if (sql.toLowerCase().startsWith("select")) {
				print(OdpsUtil.executeQuery(sql));
			} else if (sql.toLowerCase().startsWith("update")) {
				System.out.println(OdpsUtil.executeUpdate(sql));
			} else {
				print(OdpsUtil.execute(sql));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void print(ResultSet resultSet) throws SQLException {
		if (null != resultSet) {
			ResultSetMetaData metaData = resultSet.getMetaData();
			System.out.println(pickLabels(metaData));
			int count = 0;
			int columnCount = metaData.getColumnCount();
			while (resultSet.next()) {
				System.out.println(pickRow(++count, resultSet, columnCount));
			}
			System.out.println(String.format("resultCount: '%s', columnCount: '%s'", count, columnCount));
		} else {
			System.err.println("no result");
		}
	}

	private static String pickRow(int rowNo, ResultSet resultSet, int columnCount) throws SQLException {
		StringBuilder rowBuilder = new StringBuilder();
		if (columnCount > 0) {
			rowBuilder.append(rowNo).append("\t");
			for (int i = 1; i <= columnCount; i++) {
				String string = resultSet.getString(i);
				rowBuilder.append(string);
				if (i <= columnCount - 1) {
					rowBuilder.append("\t");
				}
			}
		}
		return rowBuilder.toString();
	}

	private static String pickLabels(ResultSetMetaData metaData) throws SQLException {
		StringBuilder labelBuilder = new StringBuilder();
		int columnCount = metaData.getColumnCount();
		if (columnCount > 0) {
			labelBuilder.append("no.\t");
			for (int i = 1; i <= columnCount; i++) {
				String columnLabel = metaData.getColumnLabel(i);
				labelBuilder.append(columnLabel);
				if (i <= columnCount - 1) {
					labelBuilder.append("\t");
				}
			}
		}
		return labelBuilder.toString();
	}

	private static void loadConfig() {
		String fileName = "jdbc.odps.properties";
		InputStream resourceAsStream = OdpsMain.class.getClassLoader().getResourceAsStream(fileName);
		Properties properties = new Properties();
		File file = new File(fileName);
		try {
			properties.load(resourceAsStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (file.exists()) {
			try {
				properties.load(new FileInputStream(file));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		System.out.println(String.format("加载到的配置: '%s'", properties));

		OdpsConsts.ODPS_DRIVER_NAME = properties.getProperty("odps.driver_name");
		OdpsConsts.ODPS_ACCESS_ID = properties.getProperty("odps.access_id");
		OdpsConsts.ODPS_ACCESS_KEY = properties.getProperty("odps.access_key");
		OdpsConsts.ODPS_JDBC_URL = properties.getProperty("odps.jdbc_url");
		OdpsConsts.ODPS_PROJECT = properties.getProperty("odps.project");
	}
}
