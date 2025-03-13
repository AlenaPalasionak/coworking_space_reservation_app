package org.example.coworking.service.exception;

import lombok.Getter;

@Getter
public class TimeOverlapException extends Exception {
    private final ServiceErrorCode errorCode;

    public TimeOverlapException(String message) {
        super(message);
        this.errorCode = ServiceErrorCode.TIME_OVERLAPS;
    }
}
