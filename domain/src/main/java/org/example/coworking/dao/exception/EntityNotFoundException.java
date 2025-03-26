package org.example.coworking.dao.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends Exception {
    private final DaoErrorCode errorCode;

    public EntityNotFoundException(String message, DaoErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
