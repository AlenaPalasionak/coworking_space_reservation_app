package org.example.coworking.infrastructure.dao;

import org.example.coworking.model.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationDao {
    void addReservation(Reservation reservation);

    void delete(Reservation reservation);
    Optional<Reservation> getReservationById(int reservationId);

    List<Reservation> getAllReservations();

    List<Reservation> getReservationsByCustomer(int customerId) ;

}
