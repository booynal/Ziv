/**
 * JdbcConfig.java
 */
package com.ziv.tool.jdbc;

/**
 * @author ziv
 * @date 2017年12月4日 上午11:09:08
 */
public class JdbcConfig {

	private String driverName;
	private String url;
	private String user;
	private String password;

	public JdbcConfig() {
	}

	public JdbcConfig(String driverName, String url, String user, String password) {
		this.driverName = driverName;
		this.url = url;
		this.user = user;
		this.password = password;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "JdbcConfig [driverName=" + driverName + ", url=" + url + ", user=" + user + ", password=" + password + "]";
	}

}
