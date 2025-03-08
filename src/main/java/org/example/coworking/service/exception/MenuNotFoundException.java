package org.example.coworking.service.exception;

public class MenuNotFoundException extends Exception {
    public MenuNotFoundException(String menuName) {
        super("Menu:" + menuName + " is not found");
    }
}
