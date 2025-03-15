package org.example.coworking.infrastructure.dao.exception;

import lombok.Getter;

@Getter
public class ReservationNotFoundException extends Exception {
    private final DaoErrorCode errorCode;

    public ReservationNotFoundException(String message, DaoErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
