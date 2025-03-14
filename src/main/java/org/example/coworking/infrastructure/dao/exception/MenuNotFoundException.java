package org.example.coworking.infrastructure.dao.exception;

import lombok.Getter;

@Getter
public class MenuNotFoundException extends Exception {
    private final DaoErrorCode errorCode;

    public MenuNotFoundException(String message, DaoErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
