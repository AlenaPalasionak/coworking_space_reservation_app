package org.example.coworking.infrastructure.controller.exception;

import lombok.Getter;

@Getter
public enum ControllerErrorCode {
    INVALID_INPUT("Invalid input. Try again\\n");

    private final String errorCode;

    ControllerErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return this.errorCode;
    }
}
