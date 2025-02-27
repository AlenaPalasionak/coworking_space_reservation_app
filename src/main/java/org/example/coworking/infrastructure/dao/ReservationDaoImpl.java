package org.example.coworking.infrastructure.dao;

import org.example.coworking.model.Reservation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReservationDaoImpl implements ReservationDao {
    private static final List<Reservation> reservations = new ArrayList<>();

    @Override
    public void addReservation(Reservation reservation) {
        reservation.setId(IdGenerator.generateReservationId());
        reservation.getCoworkingSpace().getReservationsPeriods().add(reservation.getPeriod());
        reservations.add(reservation);
    }

    @Override
    public void delete(Reservation reservation) {
        reservation.getCoworkingSpace().getReservationsPeriods().remove(reservation.getPeriod());
        reservations.remove(reservation);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservations;
    }

    public Optional<Reservation> getReservationById(int reservationId) {
        for (Reservation reservation : reservations) {
            if (reservation.getId() == reservationId) {
                return Optional.of(reservation);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Reservation> getReservationsByCustomer(int customerId) {
        return reservations.stream().filter(reservation -> reservation.getCustomer().getId() == customerId).collect(Collectors.toList());
    }
}
