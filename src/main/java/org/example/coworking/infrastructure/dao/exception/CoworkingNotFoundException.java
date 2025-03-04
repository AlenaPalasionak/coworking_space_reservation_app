package org.example.coworking.infrastructure.dao.exception;

public class CoworkingNotFoundException extends Exception {
    protected int coworkingId;

    public CoworkingNotFoundException(int coworkingId) {
        super("Coworking with id " + coworkingId + " is not found\n");
        this.coworkingId = coworkingId;
    }
}
