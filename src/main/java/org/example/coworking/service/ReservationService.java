package org.example.coworking.service;

import org.example.coworking.model.Coworking;
import org.example.coworking.model.Reservation;
import org.example.coworking.model.ReservationPeriod;

import java.util.List;
import java.util.Optional;

public interface ReservationService {
    void addReservation(Reservation reservation);

    void cancelReservation(int reservationId, int customerId, int coworkingId);

    List<Reservation> getAllReservations();

    List<Reservation> getReservationsByCustomer(int customerId);

    Optional<Reservation> getReservationById(int reservationId);

    void addReservationPeriod(Coworking coworking, ReservationPeriod period);

    void removeReservationPeriod(Coworking coworking, ReservationPeriod period);
}
