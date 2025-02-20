package org.example.coworking.controller;

import org.example.coworking.model.Coworking;
import org.example.coworking.model.Reservation;

import java.util.List;

public class CustomerController extends UserController {

    public CustomerController() {
        super();
    }

    public void addReservation(Reservation reservation) {
        reservationService.addReservation(reservation);
    }

    public void cancelReservation(int reservationId, int customerId, int coworkingId) {
        reservationService.cancelReservation(reservationId, customerId, coworkingId);
    }

    public List<Reservation> getReservationsByCustomer(int customerId) {
        return reservationService.getReservationsByCustomer(customerId);
    }

    public List<Coworking> getAllCoworkingSpaces() {
        return coworkingService.getAllSpaces();
    }
}
