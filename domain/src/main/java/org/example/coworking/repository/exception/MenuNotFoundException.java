package org.example.coworking.repository.exception;

import lombok.Getter;

/**
 * Exception thrown when a requested menu is not found in the storage.
 */
@Getter
public class MenuNotFoundException extends Exception {

    private final RepositoryErrorCode errorCode;

    /**
     * Constructs a new MenuNotFoundException with the specified message and error code.
     *
     * @param message   The detail message explaining why the exception was thrown.
     * @param errorCode The associated {@link RepositoryErrorCode} indicating the specific error type.
     */
    public MenuNotFoundException(String message, RepositoryErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Gets the error code associated with this exception.
     *
     * @return The {@link RepositoryErrorCode} representing the specific error.
     */
    public RepositoryErrorCode getErrorCode() {
        return errorCode;
    }
}
