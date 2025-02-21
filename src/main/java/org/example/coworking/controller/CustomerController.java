package org.example.coworking.controller;

import org.example.coworking.model.Coworking;
import org.example.coworking.model.Reservation;
import org.example.coworking.model.ReservationPeriod;

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

    public void addReservationPeriod(Coworking coworking, ReservationPeriod period) {
        reservationService.addReservationPeriod(coworking, period);
    }

    public void removeReservationPeriod(Coworking coworking, ReservationPeriod period) {
        reservationService.removeReservationPeriod(coworking, period);
    }
}
