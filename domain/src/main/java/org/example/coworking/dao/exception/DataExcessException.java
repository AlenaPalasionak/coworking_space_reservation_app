package org.example.coworking.dao.exception;

/**
 * Exception thrown when excessive or unexpected data is encountered during a database operation.
 */
public class DataExcessException extends RuntimeException {
    /**
     * Constructs new exception with message and cause
     */
    public DataExcessException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataExcessException(String message) {
        super(message);
    }
}

