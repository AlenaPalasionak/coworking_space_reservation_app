package org.example.coworking.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A utility class that provides logging functionality using Apache Log4j 2.
 * <p>
 * This class defines two static loggers:
 * - {@link #USER_OUTPUT_LOGGER} - Used for logging user-facing messages.
 * - {@link #TECHNICAL_LOGGER} - Used for logging technical messages, often for debugging or system monitoring.
 * <p>
 * Both loggers are initialized with separate loggers, allowing for distinction between user output and technical logs.
 */
public class Log {

    /**
     * Logger for user-facing messages, typically used for general application output or user notifications.
     */
    public static final Logger USER_OUTPUT_LOGGER = LogManager.getLogger("UserOutputLogger");

    /**
     * Logger for technical messages, typically used for debugging, error reporting, and system logs.
     */
    public static final Logger TECHNICAL_LOGGER = LogManager.getLogger("TechnicalLog");
}
