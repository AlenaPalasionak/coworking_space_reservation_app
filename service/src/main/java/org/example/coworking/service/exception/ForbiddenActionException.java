package org.example.coworking.service.exception;

import lombok.Getter;

@Getter
public class ForbiddenActionException extends Exception {
    private final ServiceErrorCode errorCode;

    public ForbiddenActionException(String message, ServiceErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
