package com.booynal.test.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;

public class LogBackDemo {

	private static final Logger logger = LoggerFactory.getLogger(LogBackDemo.class);

	public static void main(String[] args) {
		// 以下语句将会打印出Logger的内部状态信息
		StatusPrinter.print((LoggerContext) LoggerFactory.getILoggerFactory());
		logger.trace("跟踪");
		logger.debug("调试");
		logger.info("信息");
		logger.warn("警告");
		logger.error("错误");

	}

}
