package org.example.coworking.infrastructure.mapper.exception;

import lombok.Getter;

@Getter
public enum MapperErrorCode {
    INVALID_COWORKING_TYPE_INDEX("You entered a wrong coworking Type index."),
    INVALID_FACILITY_INDEX("You entered a wrong facility index.");
    private final String errorCode;

    MapperErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return this.errorCode;
    }
}
