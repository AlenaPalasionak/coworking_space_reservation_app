package org.example.coworking.entity.exception;

import lombok.Getter;

@Getter
public class FacilityTypeIndexException extends RuntimeException {
    private final EnumErrorCode errorCode;

    public FacilityTypeIndexException(String message, EnumErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
