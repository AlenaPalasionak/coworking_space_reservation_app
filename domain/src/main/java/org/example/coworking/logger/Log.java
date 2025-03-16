package org.example.coworking.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
    public static final Logger USER_OUTPUT_LOGGER = LogManager.getLogger("UserOutputLogger");
    public static final Logger TECHNICAL_LOGGER = LogManager.getLogger("TechnicalLog");
}
