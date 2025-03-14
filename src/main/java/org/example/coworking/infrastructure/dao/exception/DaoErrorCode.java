package org.example.coworking.infrastructure.dao.exception;

import lombok.Getter;

@Getter
public enum DaoErrorCode {
    COWORKING_IS_NOT_FOUND("Coworking is not found. Try again."),
    RESERVATION_IS_NOT_FOUND("Reservation is not found. Try again."),
    MENU_NOT_IS_FOUND("Menu is not found."),
    USER_NOT_IS_FOUND("User is not found. Try again.");

    private final String errorCode;

    DaoErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return this.errorCode;
    }
}
