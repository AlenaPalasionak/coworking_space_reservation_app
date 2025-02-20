package org.example.coworking.dao;

import org.example.coworking.model.Reservation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReservationDAOImpl implements ReservationDAO {
    private static final List<Reservation> reservations = new ArrayList<>();

    @Override
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    @Override
    public void cancelReservation(int reservationId, int customerId, int coworkingId) {
        reservations.removeIf(r -> r.getId() == reservationId && r.getCustomer().getId() == customerId);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservations;
    }

    @Override
    public List<Reservation> getReservationsByCustomer(int customerId) {
        return reservations.stream()
                .filter(reservation -> reservation.getCustomer().getId() == customerId)
                .collect(Collectors.toList());
    }

    public Optional<Reservation> getReservationById(int reservationId) {
        for (Reservation reservation : reservations) {
            if (reservation.getId() == reservationId) {
                return Optional.of(reservation);
            }
        }
        return Optional.empty(); // Возвращаем пустой Optional, если резервирование не найдено
    }
}
