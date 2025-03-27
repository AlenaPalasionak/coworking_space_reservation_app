package org.example.coworking.dao.exception;

/**
 * Exception thrown when excessive or unexpected data is encountered during a database operation.
 */
public class DataExcessException extends RuntimeException {

    /**
     * Constructs a new DataExcessException with the specified detail message.
     *
     * @param message The detail message explaining the reason for the exception.
     */
    public DataExcessException(String message) {
        super(message);
    }
}

