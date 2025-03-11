package org.example.coworking.infrastructure.controller.exception;

public class CoworkingTypeNotFoundException extends Exception {


    public CoworkingTypeNotFoundException(int coworkingTypeIndex) {
        super("You entered: " + coworkingTypeIndex + ". Wrong index.");
    }
}
