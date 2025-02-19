package org.example.coworking.service;

import org.example.coworking.model.Reservation;

import java.util.List;

public interface ReservationService {
    void addReservation(Reservation reservation);

    void cancelReservation(int id);

    List<Reservation> getAllReservations();

    List<Reservation> getReservationsByCustomer(int customerId);
}
