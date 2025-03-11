package org.example.coworking.infrastructure.dao.exception;

public class CoworkingNotFoundException extends Exception {

    public CoworkingNotFoundException(int coworkingSpaceId) {
        super("Coworking with id " + coworkingSpaceId + " is not found\n");
    }
}
