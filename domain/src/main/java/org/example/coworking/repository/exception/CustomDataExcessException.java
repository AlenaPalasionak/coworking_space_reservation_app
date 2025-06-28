package org.example.coworking.repository.exception;

/**
 * Exception thrown when excessive or unexpected data is encountered during a database operation.
 */
public class CustomDataExcessException extends RuntimeException {
    /**
     * Constructs new exception with message and cause
     */
    public CustomDataExcessException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomDataExcessException(String message) {
        super(message);
    }
}

