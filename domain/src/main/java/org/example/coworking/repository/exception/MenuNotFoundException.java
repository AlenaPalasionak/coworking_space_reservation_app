package org.example.coworking.repository.exception;

import lombok.Getter;

/**
 * Exception thrown when a requested menu is not found in the storage.
 */
@Getter
public class MenuNotFoundException extends RuntimeException {

    private final DaoErrorCode errorCode;

    /**
     * Constructs a new MenuNotFoundException with the specified message and error code.
     *
     * @param message   The detail message explaining why the exception was thrown.
     * @param errorCode The associated {@link DaoErrorCode} indicating the specific error type.
     */
    public MenuNotFoundException(String message, DaoErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Gets the error code associated with this exception.
     *
     * @return The {@link DaoErrorCode} representing the specific error.
     */
    public DaoErrorCode getErrorCode() {
        return errorCode;
    }
}
