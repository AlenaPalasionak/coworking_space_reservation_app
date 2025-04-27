package org.example.coworking.dao.exception;

import lombok.Getter;

/**
 * Exception thrown when a requested entity is not found in the database.
 */
@Getter
public class EntityNotFoundException extends Exception {

    private final DaoErrorCode errorCode;

    /**
     * Constructs a new EntityNotFoundException with the specified message and error code.
     *
     * @param message   The detail message explaining why the exception was thrown.
     * @param errorCode The associated {@link DaoErrorCode} indicating the specific error type.
     */
    public EntityNotFoundException(String message, DaoErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
