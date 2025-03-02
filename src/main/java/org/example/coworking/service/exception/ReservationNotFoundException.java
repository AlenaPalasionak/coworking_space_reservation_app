package org.example.coworking.service.exception;

public class ReservationNotFoundException extends ServiceException{
    public ReservationNotFoundException(String message) {
        super(message);
    }
}
