package org.example.coworking.infrastructure.dao.exception;

import lombok.Getter;

@Getter
public class CoworkingNotFoundException extends Exception {
    private final DaoErrorCode errorCode;

    public CoworkingNotFoundException(String message, DaoErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
