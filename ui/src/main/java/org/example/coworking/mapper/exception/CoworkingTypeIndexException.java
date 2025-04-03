package org.example.coworking.mapper.exception;

import lombok.Getter;

@Getter
public class CoworkingTypeIndexException extends Exception {
    private final MapperErrorCode errorCode;

    public CoworkingTypeIndexException(String message, MapperErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
