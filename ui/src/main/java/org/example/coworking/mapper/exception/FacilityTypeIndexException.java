package org.example.coworking.mapper.exception;

import lombok.Getter;

@Getter
public class FacilityTypeIndexException extends RuntimeException {
    private final MapperErrorCode errorCode;

    public FacilityTypeIndexException(String message, MapperErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
