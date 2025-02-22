package org.example.coworking.service;

import org.example.coworking.repository.ReservationRepository;
import org.example.coworking.repository.ReservationRepositoryImpl;
import org.example.coworking.model.Coworking;
import org.example.coworking.model.Reservation;
import org.example.coworking.model.ReservationPeriod;

import java.util.List;
import java.util.Optional;

public class ReservationServiceImpl implements ReservationService {
    ReservationRepository reservationRepository = new ReservationRepositoryImpl();

    @Override
    public void addReservation(Reservation reservation) {
        reservationRepository.addReservation(reservation);
    }

    @Override
    public void cancelReservation(int reservationId, int customerId, int coworkingId) {
        reservationRepository.cancelReservation(reservationId, customerId, coworkingId);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.getAllReservations();
    }

    @Override
    public List<Reservation> getReservationsByCustomer(int customerId) {
        return reservationRepository.getReservationsByCustomer(customerId);
    }

    @Override
    public Optional<Reservation> getReservationById(int reservationId) {
        return reservationRepository.getReservationById(reservationId);
    }

    @Override
    public void addReservationPeriod(Coworking coworking, ReservationPeriod period) {
        reservationRepository.addReservationPeriod(coworking, period);
    }

    @Override
    public void removeReservationPeriod(Coworking coworking, ReservationPeriod period) {
        reservationRepository.removeReservationPeriod(coworking, period);
    }
}
