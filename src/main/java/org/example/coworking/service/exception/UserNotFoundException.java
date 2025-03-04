package org.example.coworking.service.exception;

public class UserNotFoundException extends Exception {
    String name;

    public UserNotFoundException(String name) {
        super("User with the name " + name + " is not found. ");
        this.name = name;
    }
}
