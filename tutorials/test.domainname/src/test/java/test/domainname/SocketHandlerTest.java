package test.domainname;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

public class SocketHandlerTest {

	SocketHandler socketHandler = new SocketHandler(null);

	@Before
	public void befor() throws IOException {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("header");
		List<String> lines = IOUtils.readLines(inputStream);
		String headerString = StringUtils.join(lines, "\r\n");
		socketHandler.setHeaderString(headerString);
	}

	@Test
	public void testParseHeader() throws IOException {
		socketHandler.parseHead();
		HeaderModel headerModel = socketHandler.getHeaderModel();
		Assert.assertEquals("GET", headerModel.getMethod());
		Assert.assertEquals("/", headerModel.getPath());
		Assert.assertEquals("HTTP/1.1", headerModel.getProtocol());
		Assert.assertEquals("www.booynal.xyz", headerModel.getHost());
		Assert.assertEquals("keep-alive", headerModel.getConnection());
	}

}
