package org.example.coworking.infrastructure.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
    public static final Logger CONSOLE_LOGGER = LogManager.getLogger("ConsoleLogger");
    public static final Logger FILE_LOGGER = LogManager.getLogger("FileLog");
}
