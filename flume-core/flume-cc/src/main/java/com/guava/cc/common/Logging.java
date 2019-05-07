package com.guava.cc.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: chenchong
 * Date: 2019/2/25
 * description:
 */
public abstract class Logging {

	protected final String loggerName = getClass().getName();

	protected final Logger logger = LoggerFactory.getLogger(loggerName);

	protected String logIdent = ident();

	protected String msgWithLogIdent(String msg) {
		return logIdent == null ? msg : logIdent + msg;
	}

	protected void info(String msg) {
		logger.info(msgWithLogIdent(msg));
	}

	protected void info(String msg, Throwable t) {
		logger.info(msgWithLogIdent(msg), t);
	}

	protected void debug(String msg) {
		logger.debug(msgWithLogIdent(msg));
	}

	protected void debug(String msg, Throwable t) {
		logger.debug(msgWithLogIdent(msg), t);
	}

	protected void error(String msg) {
		logger.error(msgWithLogIdent(msg));
	}

	protected void error(String msg, Throwable t) {
		logger.error(msgWithLogIdent(msg), t);
	}

	protected abstract String ident();

}
