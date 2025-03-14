package org.example.coworking.service.exception;

import lombok.Getter;

@Getter
public class TimeOverlapException extends Exception {
    private final ServiceErrorCode errorCode;

    public TimeOverlapException(String message, ServiceErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
