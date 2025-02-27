package org.example.coworking.service.exception;

public class TimeOverlapException extends ServiceException{
    public TimeOverlapException(String message) {
        super(message);
    }
}
