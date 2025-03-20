package org.example.coworking.mapper.exception;

import lombok.Getter;

@Getter
public class CoworkingTypeIndexException extends Exception {
    private final MapperErrorCode errorCode;

    public CoworkingTypeIndexException(String message) {
        super(message);
        this.errorCode = MapperErrorCode.INVALID_COWORKING_TYPE_INDEX;
    }
}
