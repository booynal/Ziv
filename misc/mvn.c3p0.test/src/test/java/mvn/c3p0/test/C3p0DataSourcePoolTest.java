package mvn.c3p0.test;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import test.junit.base.BaseTest;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3p0DataSourcePoolTest extends BaseTest {

	private static ComboPooledDataSource dataSource;

	@BeforeClass
	public static void createPoo() throws PropertyVetoException {
		String driverClass = "com.mysql.jdbc.Driver";
		String jdbcUrl = "jdbc:mysql://localhost:3306/mysql";
		String user = "root";
		String password = "root";

		dataSource = new ComboPooledDataSource();
		dataSource.setDriverClass(driverClass);
		dataSource.setJdbcUrl(jdbcUrl);
		dataSource.setUser(user);
		dataSource.setPassword(password);

		// 初始化连接池中的连接数，取值应在minPoolSize与maxPoolSize之间，默认值：3
		dataSource.setInitialPoolSize(1);
		// 连接池中保留的最小连接数，默认为：3
		dataSource.setMinPoolSize(1);
		// 连接池中保留的最大连接数。默认值: 15
		dataSource.setMaxPoolSize(3);
		// 最大片段(语句)数量。
		dataSource.setMaxStatements(10);
		// 最大空闲时间，单位：秒。若为0则永不丢弃。默认值: 0
		dataSource.setMaxIdleTime(10);
		// 当连接池中的连接耗尽的时候c3p0一次同时获取的连接数。默认值: 3
		dataSource.setAcquireIncrement(1);
	}

	@AfterClass
	public static void tearDown() {
		dataSource.close();
	}

	@Test
	public void test() throws PropertyVetoException, SQLException {
		System.out.println("连通性测试。。。");
		// 如果连接池空了，则阻塞
		Connection connection = dataSource.getConnection();
		System.out.println(connection);
		// 查询出当前数据库中的所有用户
		String sql = "select * from user";
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		ResultSet resultSet = prepareStatement.executeQuery();
		System.out.println(resultSet);
		if (null != resultSet) {
			while (resultSet.next()) {
				System.out.printf("%s, %s, %s\n", resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
			}
			resultSet.close();
		}
		prepareStatement.close();

		// 该方法已经被C3P0包装过，实际上执行的是将连接对象放回池子，并非真正关闭连接
		connection.close();
	}

	@Test
	public void test_getMenyConnections() throws InterruptedException {
		System.out.println("并发测试。。。");
		System.out.println("连接池最大只有3个，而这里用5个线程同时获取连接，那么必定有3个最先获取到，剩余2个线程需要等待前面的线程释放连接");
		for (int i = 0; i < 5; i++) {
			/**
			 * 连接池最大只有3个，而这里用5个线程同时获取连接，那么必定有3个最先获取到，剩余2个线程需要等待前面的线程释放连接
			 */
			new Thread(() -> {
				try {
					Connection connection = dataSource.getConnection();
					System.out.println(connection);
					System.out.println("get: " + connection.hashCode() % 100);
					Thread.sleep(1000);
					connection.close();
					System.out.println("return: " + connection.hashCode() % 100);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}, "t" + i).start();
		}
		Thread.sleep(5000);
		System.out.println("Done");
	}

}
