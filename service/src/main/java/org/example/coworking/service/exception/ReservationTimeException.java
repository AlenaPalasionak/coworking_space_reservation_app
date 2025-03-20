package org.example.coworking.service.exception;

import lombok.Getter;

@Getter
public class ReservationTimeException extends Exception {
    private final ServiceErrorCode errorCode;

    public ReservationTimeException(String message, ServiceErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
