package org.example.coworking.controller.exception;

import lombok.Getter;

@Getter
public class InvalidInputException extends Exception {
    private final ControllerErrorCode errorCode;

    public InvalidInputException(String message, ControllerErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
