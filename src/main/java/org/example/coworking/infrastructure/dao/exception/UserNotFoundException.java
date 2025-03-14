package org.example.coworking.infrastructure.dao.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends Exception {
    private final DaoErrorCode errorCode;

    public UserNotFoundException(String message) {
        super(message);
        this.errorCode = DaoErrorCode.USER_NOT_IS_FOUND;
    }
}
