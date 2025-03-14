package org.example.coworking.service.exception;

import lombok.Getter;

@Getter
public enum ServiceErrorCode {
    FORBIDDEN_ACTION("Action is forbidden. Try again."),
    INVALID_TIME_LOGIC("Error in time logic. Try again."),
    TIME_OVERLAPS("Time overlaps with another time.");

    private final String errorCode;

    ServiceErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return this.errorCode;
    }
}
