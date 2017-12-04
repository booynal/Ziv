package com.ziv.tool.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcUtil {

	private final static String actionName = "JDBC-SQL执行工具";

	private static JdbcConfig config;

	static {
		init();
	}

	private static void init() {
		if (null == config) {
			config = JdbcConfigLoader.loadConfig();
		}
		try {
			Class.forName(config.getDriverName());
		} catch (ClassNotFoundException e) {
			System.err.println(String.format("%s-未找到驱动类: '%s'", actionName, config.getDriverName()));
		}
	}

	public static int executeUpdate(String sql) throws SQLException {
		Connection conn = createConnection();
		Statement statement = null;
		try {
			statement = conn.createStatement();
			System.out.println(String.format("%s-准备执行update-sql: '%s'", actionName, sql));
			return statement.executeUpdate(sql);
		} catch (Exception e) {
			System.out.println(String.format("%s-执行update-sql出错:", actionName));
			e.printStackTrace();
		} finally {
			close(statement);
		}
		return -1;
	}

	public static ResultSet executeQuery(String sql) throws SQLException {
		Connection conn = createConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			// ps.setFetchSize(Integer.MIN_VALUE); // hive不支持设置小于0的值
			System.out.println(String.format("%s-准备执行query-sql: '%s'", actionName, sql));
			return ps.executeQuery();
		} catch (Exception e) {
			System.out.println(String.format("%s-执行query-sql出错:", actionName));
			e.printStackTrace();
			close(ps);
		}
		return null;
	}

	public static ResultSet execute(String sql) throws SQLException {
		if (null != sql && sql.isEmpty() == false) {
			Connection conn = createConnection();
			Statement statement = null;
			try {
				statement = conn.createStatement();
				System.out.println(String.format("%s-准备执行-sql: '%s'", actionName, sql));
				statement.execute(sql);
				return statement.getResultSet();
			} catch (Exception e) {
				System.err.println(String.format("%s-执行sql出错", actionName, e.getMessage()));
				e.printStackTrace();
				close(statement);
			}
		}
		return null;
	}

	private static Connection createConnection() throws SQLException {
		Connection conn = DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
		if (conn.isClosed()) {
			System.err.println(String.format("%s-failed to connect: '%s'!", actionName, config.getUrl()));
		} else {
			System.out.println(String.format("%s-Successed to connect: '%s'", actionName, config.getUrl()));
		}
		return conn;
	}

	public static void close(AutoCloseable... closeables) {
		if (null != closeables) {
			for (AutoCloseable closeable : closeables) {
				try {
					closeable.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
