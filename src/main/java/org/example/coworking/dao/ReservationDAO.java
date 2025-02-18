package org.example.coworking.dao;

import org.example.coworking.model.Reservation;

import java.util.List;

public interface ReservationDAO {
    void addReservation(Reservation reservation);

    void cancelReservation(int id);

    List<Reservation> getAllReservations();
}
