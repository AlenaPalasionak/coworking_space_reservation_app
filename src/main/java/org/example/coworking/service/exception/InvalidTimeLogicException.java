package org.example.coworking.service.exception;

import lombok.Getter;

@Getter
public class InvalidTimeLogicException extends Exception {
    private final ServiceErrorCode errorCode;

    public InvalidTimeLogicException(String message) {
        super(message);
        this.errorCode = ServiceErrorCode.INVALID_TIME_LOGIC;
    }
}
