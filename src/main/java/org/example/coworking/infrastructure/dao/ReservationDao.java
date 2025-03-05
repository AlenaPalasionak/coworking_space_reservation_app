package org.example.coworking.infrastructure.dao;

import org.example.coworking.infrastructure.dao.exception.ReservationNotFoundException;
import org.example.coworking.model.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationDao {
    void addReservation(Reservation reservation);
    void delete(Reservation reservation) throws ReservationNotFoundException;
    Optional<Reservation> getReservationById(int reservationId) throws ReservationNotFoundException;
    List<Reservation> getAllReservations();
    List<Reservation> getReservationsByCustomer(int customerId);
    void load();
    void save();
}
