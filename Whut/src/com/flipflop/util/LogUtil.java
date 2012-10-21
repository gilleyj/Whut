package com.flipflop.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LogUtil {
	public static void initRootLogger(Logger logger) {
		LogManager.getLogManager().reset();
		ConsoleHandler ch = new ConsoleHandler();
		ch.setFormatter(new SimplerFormatter());
		logger.addHandler(ch);
	}
}
