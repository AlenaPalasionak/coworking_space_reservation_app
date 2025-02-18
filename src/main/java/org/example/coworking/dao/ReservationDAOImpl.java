package org.example.coworking.dao;

import org.example.coworking.model.Reservation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationDAOImpl implements ReservationDAO {
    private List<Reservation> reservations = new ArrayList<>();

    @Override
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    @Override
    public void cancelReservation(int id) {
        reservations = reservations.stream()
                .filter(reservation -> reservation.getId() != id)
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservations;
    }
}
