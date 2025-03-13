package org.example.coworking.infrastructure.dao.exception;

import lombok.Getter;

@Getter
public class CoworkingNotFoundException extends Exception {
    private final DaoErrorCode errorCode;

    public CoworkingNotFoundException(String message) {
        super(message);
        this.errorCode = DaoErrorCode.COWORKING_NOT_FOUND;
    }
}
