package com.snipe.learning.utility;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggerUtil {

    private static final Log LOGGER = LogFactory.getLog(LoggerUtil.class);

    public void logInfo(String message) {
        LOGGER.info(message);
    }

    public void logError(String message, Exception e) {
        LOGGER.error(message, e);
    }
    
    public void logError(Exception e) {
        LOGGER.error(e);
    }
}
