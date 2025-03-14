package org.example.coworking.service.exception;

import lombok.Getter;

@Getter
public class InvalidTimeLogicException extends Exception {
    private final ServiceErrorCode errorCode;

    public InvalidTimeLogicException(String message, ServiceErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
