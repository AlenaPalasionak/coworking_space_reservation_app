package org.example.coworking.controller;

import org.example.coworking.model.Coworking;
import org.example.coworking.model.Reservation;

import java.util.List;

public class CustomerController extends AbstractUserController {

    public CustomerController() {
        super();
    }

    public List<Coworking> getAllCoworkingSpaces() {
        return coworkingService.getAllSpaces();
    }

    public void addReservation(Reservation reservation) {
        reservationService.addReservation(reservation);
    }

    public void canselReservation(int id) {
        reservationService.cancelReservation(id);
    }

    public List<Reservation> getReservationsByCustomer(int customerId) {
        return reservationService.getReservationsByCustomer(customerId);
    }
}
