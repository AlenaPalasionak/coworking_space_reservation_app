package org.example.coworking.repository.exception;

import lombok.Getter;

/**
 * Exception thrown when a requested entity is not found in the database.
 */
@Getter
public class EntityNotFoundException extends RuntimeException {

    private final RepositoryErrorCode errorCode;

    /**
     * Constructs a new EntityNotFoundException with the specified message and error code.
     *
     * @param message   The detail message explaining why the exception was thrown.
     * @param errorCode The associated {@link RepositoryErrorCode} indicating the specific error type.
     */
    public EntityNotFoundException(String message, RepositoryErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
