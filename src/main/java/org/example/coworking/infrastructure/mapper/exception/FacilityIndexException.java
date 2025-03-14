package org.example.coworking.infrastructure.mapper.exception;

import lombok.Getter;

@Getter
public class FacilityIndexException extends RuntimeException {
    private final MapperErrorCode errorCode;

    public FacilityIndexException(String message) {
        super(message);
        this.errorCode = MapperErrorCode.INVALID_FACILITY_INDEX;
    }
}
