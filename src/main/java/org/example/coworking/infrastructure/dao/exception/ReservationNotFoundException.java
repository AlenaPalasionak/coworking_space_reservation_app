package org.example.coworking.infrastructure.dao.exception;

public class ReservationNotFoundException extends Exception {
    protected int reservationId;

    public ReservationNotFoundException(int reservationId) {
        super("Reservation with id " + reservationId + " is not found");
        this.reservationId = reservationId;
    }
}
