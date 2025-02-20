package org.example.coworking.dao;

import org.example.coworking.model.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationDAO {
    void addReservation(Reservation reservation);

    void cancelReservation(int reservationId, int customerId, int coworkingId);

    List<Reservation> getAllReservations();

    List<Reservation> getReservationsByCustomer(int customerId);

    Optional<Reservation> getReservationById(int reservationId);
}
