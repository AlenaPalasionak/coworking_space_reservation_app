package org.example.coworking.infrastructure.mapper.exception;

public class FacilityIndexException extends Throwable {
    public FacilityIndexException(int facilitiesIndex) {
        super("You entered: " + facilitiesIndex + ". Wrong index.");
    }
}
