package mvn.db.pool.druid.test;

import static com.alibaba.druid.pool.DruidDataSourceFactory.PROP_PASSWORD;
import static com.alibaba.druid.pool.DruidDataSourceFactory.PROP_URL;
import static com.alibaba.druid.pool.DruidDataSourceFactory.PROP_USERNAME;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * Created by ziv on 16/12/13.
 */
public class DruidDataSourceTest {

    public static void main(String[] args) throws Exception {
        String sql = "select count(1) from user";
        int testCount = 1000;

        System.out.println("c3p0:");
        DataSource c3p0DataSource = getC3p0DataSource();
        Connection c3p0DataSourceConnection = c3p0DataSource.getConnection();
        System.out.println(c3p0DataSourceConnection);
        testGetCount(sql, c3p0DataSourceConnection, testCount);

        System.out.println("druid:");
        DataSource dataSource = getDruidDataSource();
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
        testGetCount(sql, connection, testCount);

    }

    private static void testGetCount(String sql, Connection connection, int testCount) throws SQLException {
        Object result = getCount(connection, sql);
        System.out.println(result);
        long start = System.currentTimeMillis();
        for (int i = 0; i < testCount; i++) {
            getCount(connection, sql);
            if (i % 100 == 0) {
                System.out.println(String.format("%s - cost: '%s'", i, (System.currentTimeMillis() - start)));
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("cost: " + (end - start));
    }

    private static DataSource getC3p0DataSource() throws Exception {
        String driverClass = "com.mysql.jdbc.Driver";
        String jdbcUrl = "jdbc:mysql://localhost:3306/mysql";
        String user = "root";
        String password = "root";

        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(driverClass);
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUser(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    private static DataSource getDruidDataSource() throws Exception {
        Map<String, String> prop = new HashMap<>();
        prop.put(PROP_URL, "jdbc:mysql://localhost:3306/test");
        prop.put(PROP_USERNAME, "root");
        prop.put(PROP_PASSWORD, "root");
        return DruidDataSourceFactory.createDataSource(prop);
    }

    private static Object getCount(Connection connection, String sql) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getObject(1);
        }
        return null;
    }
}
