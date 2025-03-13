package org.example.coworking.infrastructure.dao.exception;

import lombok.Getter;

@Getter
public enum DaoErrorCode {
    COWORKING_NOT_FOUND("Coworking with is not found. Try again\n"),
    RESERVATION_NOT_FOUND("Reservation with id is not found. Try again\n"),
    MENU_NOT_FOUND("Menu is not found. "),
    USER_NOT_FOUND("User is not found. Try again\n");

    private final String errorCode;

    DaoErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    @Override
    public String toString() {
        return this.errorCode;
    }
}
