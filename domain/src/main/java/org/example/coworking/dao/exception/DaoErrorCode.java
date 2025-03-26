package org.example.coworking.dao.exception;

import lombok.Getter;

@Getter
public enum DaoErrorCode {
    COWORKING_IS_NOT_FOUND("Coworking is not found. Try again."),
    RESERVATION_IS_NOT_FOUND("Reservation is not found. Try again."),
    MENU_IS_NOT_FOUND("Menu is not found."),
    USER_IS_NOT_FOUND("User is not found. Try again."),
    COWORKING_SPACE_INSERTION_FAILURE("Failed to add coworking space"),
    COWORKING_TYPE_ID_GETTING_FAILURE("Failed to get coworking type id"),
    COWORKING_SPACE_ID_GETTING_FAILURE("Failed to get a new coworking space Id"),
    OBJECT_FIELD_IS_NOT_FOUND("Element is not found"),
    CONNECTION_FAILURE("Connection failure");

    private final String errorCode;

    DaoErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return this.errorCode;
    }
}
