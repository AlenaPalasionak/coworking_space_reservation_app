package org.example.coworking.repository.exception;

import lombok.Getter;

/**
 * Enum representing various DAO (Data Access Object) error codes.
 */
@Getter
public enum RepositoryErrorCode {
    /**
     * Error when coworking space is not found.
     */
    COWORKING_IS_NOT_FOUND("Coworking is not found. Try again."),

    /**
     * Error when reservation is not found.
     */
    RESERVATION_IS_NOT_FOUND("Reservation is not found. Try again."),

    /**
     * Error when menu is not found.
     */
    MENU_IS_NOT_FOUND("Menu is not found."),

    /**
     * Error when user is not found.
     */
    USER_IS_NOT_FOUND("User is not found. Try again.");

    private final String errorCode;

    /**
     * Constructor for RepositoryErrorCode.
     *
     * @param errorCode The error message associated with the error code.
     */
    RepositoryErrorCode(String errorCode) {
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
