package org.example.coworking.infrastructure.dao.exception;

public class ReservationNotFoundException extends Exception {

    public ReservationNotFoundException(int reservationId) {
        super("Reservation with id " + reservationId + " is not found");
    }
}
