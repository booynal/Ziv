package test.domainname;

import java.io.IOException;
import java.net.ServerSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDomainName {

	private static Logger logger = LoggerFactory.getLogger(TestDomainName.class);

	private int listeningPort = 80;

	public static void main(String[] args) {
		logger.info("main run...");
		new TestDomainName().launch();
		logger.info("main run done.");
	}

	private void launch() {
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(listeningPort);
			logger.info("binding ok: port=" + listeningPort);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			while (true) {
				logger.info("accout clients...");
				new SocketHandler(ss.accept()).handle();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (null != ss) {
			try {
				ss.close();
			} catch (IOException e) {
			}
		}
	}

}
