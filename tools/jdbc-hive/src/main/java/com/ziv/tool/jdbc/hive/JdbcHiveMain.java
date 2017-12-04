/**
 * JdbcHiveMain.java
 */
package com.ziv.tool.jdbc.hive;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;

import com.ziv.tool.jdbc.JdbcUtil;

/**
 * @author ziv
 * @date 2017年12月4日 上午10:53:55
 */
public class JdbcHiveMain {

	public static void main(String[] args) {
		if (args.length <= 0) {
			System.err.println("Usage: " + JdbcHiveMain.class.getName() + " sql");
			System.exit(-1);
		}

		String sql = StringUtils.join(args, ' ');
		System.out.println(String.format("sql: '%s'", sql));
		try {
			if (sql.toLowerCase().startsWith("select")) {
				print(JdbcUtil.executeQuery(sql));
			} else if (sql.toLowerCase().startsWith("update")) {
				System.out.println(JdbcUtil.executeUpdate(sql));
			} else {
				print(JdbcUtil.execute(sql));
			}
		} catch (Exception e) {
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

}
