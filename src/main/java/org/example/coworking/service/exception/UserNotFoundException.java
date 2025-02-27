package org.example.coworking.service.exception;

public class UserNotFoundException extends ServiceException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
