package org.example.coworking.service.exception;

public class ForbiddenActionException extends ServiceException {

    public ForbiddenActionException(String message) {
        super(message);
    }
}
