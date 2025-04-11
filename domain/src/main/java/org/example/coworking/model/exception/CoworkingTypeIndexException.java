package org.example.coworking.model.exception;

import lombok.Getter;

@Getter
public class CoworkingTypeIndexException extends RuntimeException {
    private final EnumErrorCode errorCode;

    public CoworkingTypeIndexException(String message, EnumErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
