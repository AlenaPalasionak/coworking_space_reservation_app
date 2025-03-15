package org.example.coworking.infrastructure.controller.exception;

import lombok.Getter;

@Getter
public class InvalidInputException extends Exception {
    private final ControllerErrorCode errorCode;

    public InvalidInputException(String message, ControllerErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
