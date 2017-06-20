package com.cci.gpec.metier;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public aspect AspectMetier {
	
	public enum Level {INFO, DEBUG, ERROR};
	private static final boolean LONG_STRING_MSG = true;
	
	pointcut logError():
		execution(* *(..)) && !within(AspectMetier);

	Object around() : logError() {
		Object o = null;
		try {
			o = proceed();
		} catch (Exception e) {
			logging(thisJoinPoint, Level.ERROR, e.toString());
		}
		return o;
	}

	pointcut logDebug():
		execution(* *(..)) && !within(AspectMetier);

	after():logDebug() {
		String msg = null;
		if(LONG_STRING_MSG) {
			msg = thisJoinPoint.getSignature().toLongString();
		} else {
			msg = thisJoinPoint.getSignature().toShortString();
		}
		
		logging(thisJoinPoint, Level.DEBUG, msg);
	}
	
	private void logging(JoinPoint joinPoint, Level level, String msg) {
		Object object = joinPoint.getTarget();
		if(object != null) {
			// Create the logger
			Logger logger = LoggerFactory.getLogger(object.getClass().getName());
		
			// Allows to get the number line in the ConversionPattern of log4j with the value %X{lineNumber}
			MDC.put("lineNumber", Integer.valueOf(joinPoint.getSourceLocation().getLine()).toString());
		
			// Logging
			if(level.equals(Level.ERROR) && logger.isErrorEnabled()) {
				logger.error(msg);
			} else if(level.equals(Level.DEBUG) && logger.isDebugEnabled()) {
				logger.debug(msg);
			} else if (level.equals(Level.INFO) && logger.isInfoEnabled()) {
				logger.info(msg);
			}
		
			MDC.remove("lineNumber");
		}
	}

}