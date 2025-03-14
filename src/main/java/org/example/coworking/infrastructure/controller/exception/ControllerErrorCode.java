package org.example.coworking.infrastructure.controller.exception;

import lombok.Getter;

@Getter
public enum ControllerErrorCode {
    INVALID_INPUT("Invalid input. Try again");

    private final String controllerErrorCode;

    ControllerErrorCode(String controllerErrorCode) {
        this.controllerErrorCode = controllerErrorCode;
    }

    @Override
    public String toString() {
        return this.controllerErrorCode;
    }
}
