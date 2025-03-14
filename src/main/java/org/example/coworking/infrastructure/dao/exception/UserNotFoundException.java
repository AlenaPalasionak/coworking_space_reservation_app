package org.example.coworking.infrastructure.dao.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends Exception {
    private final DaoErrorCode errorCode;

    public UserNotFoundException(String message, DaoErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
