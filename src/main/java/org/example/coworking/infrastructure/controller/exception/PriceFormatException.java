package org.example.coworking.infrastructure.controller.exception;

public class PriceFormatException extends Exception {

    public PriceFormatException(String price) {
        super("You entered: " + price + ". Wrong format for price. Please, use only numbers");
    }
}
