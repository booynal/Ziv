package test.domainname;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketHandler implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(SocketHandler.class);

	private Socket socket;
	private String headerString;

	private HeaderModel headerModel = new HeaderModel();

	public SocketHandler(Socket socket) {
		this.socket = socket;
	}

	public void handle() {
		new Thread(this).start();
	}

	@Override
	public void run() {
		logger.debug(socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
		readHeadString(socket);
		parseHead();
		output();
	}

	private void output() {
		if (StringUtils.equals("/favicon.ico", headerModel.getPath())) {
			InputStream input = getClass().getClassLoader().getResourceAsStream("favicon.ico");
			OutputStream output;
			try {
				output = socket.getOutputStream();
				IOUtils.copy(input, output);
				output.flush();
				output.close();
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			OutputStream outputStream = socket.getOutputStream();
			StringBuilder message = new StringBuilder();
			message.append("<html>");
			message.append("你好，已收到您的请求，当前服务器时间为：" + DateFormat.getDateInstance(DateFormat.FULL, Locale.CHINA).format(new Date()));
			message.append("<br/>");
			message.append("您的IP是：" + socket.getRemoteSocketAddress());
			message.append("<br/>");
			message.append("<br/>");
			message.append("<div align='center' >");
			message.append(getForm());
			message.append("<div>");
			message.append("</html>");
			outputStream.write(message.toString().getBytes());
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void parseHead() {
		System.out.println("head: " + headerString);
		if (StringUtils.isEmpty(headerString)) {
			return;
		}
		String[] lines = StringUtils.split(headerString, "\r\n");
		String firstLine = lines[0];
		String[] firstLineSplit = StringUtils.split(firstLine, " ");
		headerModel.setMethod(firstLineSplit[0]);
		headerModel.setPath(firstLineSplit[1]);
		headerModel.setProtocol(firstLineSplit[2]);
		for (int i = 1; i < lines.length; i++) {
			String line = lines[i];
			String value = StringUtils.substringAfter(line, ": ");
			if (StringUtils.startsWith(line, HeaderConsts.HOST)) {
				headerModel.setHost(value);
			} else if (StringUtils.startsWith(line, HeaderConsts.CONNECTION)) {
				headerModel.setConnection(value);
			} else if (StringUtils.startsWith(line, HeaderConsts.ACCEPT)) {
				headerModel.setAccept(value);
			} else if (StringUtils.startsWith(line, HeaderConsts.ACCEPT_ENCODING)) {
				headerModel.setAcceptEncoding(value);
			} else if (StringUtils.startsWith(line, HeaderConsts.ACCEPT_LANGUAGE)) {
				headerModel.setAcceptLanguange(value);
			} else if (StringUtils.startsWith(line, HeaderConsts.UPGRADE_INSECURE_REQUESTS)) {
				headerModel.setUpgradeInsecureRequest(value);
			} else if (StringUtils.startsWith(line, HeaderConsts.USER_AGENT)) {
				headerModel.setUserAgent(value);
			}
		}
	}

	private String readHeadString(Socket socket) {
		try {
			InputStream inputStream = socket.getInputStream();
			int len = -1;
			byte[] b = new byte[1024];
			StringBuilder sb = new StringBuilder();
			boolean first = true;
			try {
				while (checkAvailable(socket, inputStream, first) && -1 != (len = inputStream.read(b))) {
					sb.append(new String(b, 0, len));
					first = false;
				}
			} catch (Exception e) {
			}
			headerString = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return headerString;
	}

	private boolean checkAvailable(Socket socket, InputStream inputStream, boolean first) throws IOException {
		if (first) {
			if (inputStream.available() <= 0) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
				}
			}
			if (inputStream.available() <= 0) {
				return false;
			}
			return socket.isClosed() == false;
		}
		return socket.isClosed() == false && inputStream.available() > 0;
	}

	protected CharSequence getForm() {
		StringBuilder message = new StringBuilder();
		message.append("<h1>问卷调查</h1>");
		message.append("<form action='http://www.baidu.com/s' method='GET' >");
		message.append("你是【大笨蛋】还是【大傻瓜】呢？");
		message.append("<br />");
		message.append("<for id='wd1'>");
		message.append("<input id='wd1' name='wd' type='radio' value='大笨蛋' checked='checked' >前者</input>");
		message.append("</for>");
		message.append("<for id='wd2'>");
		message.append("<input id='wd2' name='wd' type='radio' value='大傻瓜' >后者</input>");
		message.append("</for>");
		message.append("<br />");
		message.append("<input type='submit' />");
		message.append("</form>");

		return message;
	}

	public String getHeaderString() {
		return headerString;
	}

	public void setHeaderString(String headerString) {
		this.headerString = headerString;
	}

	public HeaderModel getHeaderModel() {
		return headerModel;
	}

	public void setHeaderModel(HeaderModel headerModel) {
		this.headerModel = headerModel;
	}

}
