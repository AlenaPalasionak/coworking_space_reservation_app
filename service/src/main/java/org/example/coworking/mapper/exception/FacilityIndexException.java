package org.example.coworking.mapper.exception;

import lombok.Getter;

@Getter
public class FacilityIndexException extends RuntimeException {
    private final MapperErrorCode errorCode;

    public FacilityIndexException(String message, MapperErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
