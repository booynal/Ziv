package test.domainname;

public class HeaderModel {

	private String method;
	private String path;
	private String protocol;
	private String host;
	private String connection;
	private String accept;
	private String upgradeInsecureRequest;
	private String userAgent;
	private String acceptEncoding;
	private String acceptLanguange;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getConnection() {
		return connection;
	}

	public void setConnection(String connection) {
		this.connection = connection;
	}

	public String getAccept() {
		return accept;
	}

	public void setAccept(String accept) {
		this.accept = accept;
	}

	public String getUpgradeInsecureRequest() {
		return upgradeInsecureRequest;
	}

	public void setUpgradeInsecureRequest(String upgradeInsecureRequest) {
		this.upgradeInsecureRequest = upgradeInsecureRequest;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getAcceptLanguange() {
		return acceptLanguange;
	}

	public void setAcceptLanguange(String acceptLanguange) {
		this.acceptLanguange = acceptLanguange;
	}

	public String getAcceptEncoding() {
		return acceptEncoding;
	}

	public void setAcceptEncoding(String acceptEncoding) {
		this.acceptEncoding = acceptEncoding;
	}

	@Override
	public String toString() {
		return "HeaderModel [method=" + method + ", path=" + path + ", protocol=" + protocol + ", host=" + host + ", connection=" + connection + ", accept=" + accept + ", upgradeInsecureRequest="
				+ upgradeInsecureRequest + ", userAgent=" + userAgent + ", acceptEncoding=" + acceptEncoding + ", acceptLanguange=" + acceptLanguange + "]";
	}

}
