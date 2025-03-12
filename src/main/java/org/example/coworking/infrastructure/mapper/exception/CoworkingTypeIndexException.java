package org.example.coworking.infrastructure.mapper.exception;

public class CoworkingTypeIndexException extends Exception {


    public CoworkingTypeIndexException(int coworkingTypeIndex) {
        super("You entered: " + coworkingTypeIndex + ". Wrong index.");
    }
}
