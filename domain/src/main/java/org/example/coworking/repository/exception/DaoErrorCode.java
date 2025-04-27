package org.example.coworking.repository.exception;

import lombok.Getter;

/**
 * Enum representing various DAO (Data Access Object) error codes.
 */
@Getter
public enum DaoErrorCode {
    /**
     * Error when coworking space is not found.
     */
    COWORKING_IS_NOT_FOUND("Coworking is not found. Try again."),

    /**
     * Error when reservation is not found.
     */
    RESERVATION_IS_NOT_FOUND("Reservation is not found. Try again."),

        /**
     * Error when user is not found.
     */
    USER_IS_NOT_FOUND("User is not found. Try again.");

    private final String errorCode;

    /**
     * Constructor for DaoErrorCode.
     *
     * @param errorCode The error message associated with the error code.
     */
    DaoErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Returns the error message as a string.
     *
     * @return The error message.
     */
    @Override
    public String toString() {
        return this.errorCode;
    }
}
