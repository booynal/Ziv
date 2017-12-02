package com.ziv.tool.odps;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OdpsUtil {

	private static Logger LOGGER = LoggerFactory.getLogger(OdpsUtil.class);
	private final static String actionName = "odps-sql执行工具";
	private static Connection conn;

	public static int executeUpdate(String sql) throws SQLException {
		conn = init();
		Statement statement = null;
		try {
			statement = conn.createStatement();
			LOGGER.info(String.format("%s-准备执行sql ： %s", actionName, sql));
			int executeUpdate = statement.executeUpdate(sql);
			statement.close();
			return executeUpdate;
		} catch (Exception e) {
			String message = String.format("%s-odps执行sql出错", actionName, e.getMessage());
			LOGGER.error(message, e);
			if (statement != null) {
				statement.close();
			}
		}
		return -1;
	}

	public static ResultSet executeQuery(String sql) throws SQLException {
		conn = init();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			ps.setFetchSize(Integer.MIN_VALUE);
			LOGGER.info(String.format("%s-准备执行sql ： %s", actionName, sql));
			return ps.executeQuery();
		} catch (Exception e) {
			String message = String.format("%s-odps执行sql出错", actionName, e.getMessage());
			LOGGER.error(message, e);
			if (ps != null) {
				ps.close();
			}
		}
		return null;
	}

	public static ResultSet execute(String sql) throws SQLException {
		if (!StringUtils.isEmpty(sql)) {
			conn = init();
			Statement statement = null;
			try {
				statement = conn.createStatement();
				LOGGER.info(String.format("%s-准备执行sql ： %s", actionName, sql));
				statement.execute(sql);
				ResultSet resultSet = statement.getResultSet();
				return resultSet;
			} catch (Exception e) {
				String message = String.format("%s-odps执行sql出错", actionName, e.getMessage());
				LOGGER.error(message, e);
				if (statement != null) {
					statement.close();
				}
			}
		}
		return null;
	}

	private static Connection init() throws SQLException {
		try {
			Class.forName(OdpsConsts.ODPS_DRIVER_NAME);
		} catch (ClassNotFoundException e) {
			String message = String.format("%s-odps连接sql - jdbc初始化驱动出错", actionName, e.getMessage());
			LOGGER.error(message, e);
		}

		Properties config = new Properties();
		config.put("access_id", OdpsConsts.ODPS_ACCESS_ID);
		config.put("access_key", OdpsConsts.ODPS_ACCESS_KEY);
		config.put("project_name", OdpsConsts.ODPS_PROJECT);
		config.put("charset", "utf-8");
		Connection conn = DriverManager.getConnection(OdpsConsts.ODPS_JDBC_URL, config);
		if (!conn.isClosed()) {
			LOGGER.info(String.format("%s-Succeeded connecting to the Database!", actionName));
		} else {
			LOGGER.error(String.format("%s-connect filed!", actionName));
		}
		return conn;
	}

	public static void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			String message = String.format("%s-关闭odps - jdbc出错", actionName, e.getMessage());
			LOGGER.error(message, e);
		}
	}
}
