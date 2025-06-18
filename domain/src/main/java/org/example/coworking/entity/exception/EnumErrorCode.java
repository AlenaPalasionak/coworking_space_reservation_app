package org.example.coworking.entity.exception;

import lombok.Getter;

@Getter
public enum EnumErrorCode {
    INVALID_COWORKING_TYPE_INDEX("You entered a wrong coworking Type index."),
    INVALID_FACILITY_INDEX("You entered a wrong facility index.");
    private final String errorCode;

    EnumErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return this.errorCode;
    }
}
