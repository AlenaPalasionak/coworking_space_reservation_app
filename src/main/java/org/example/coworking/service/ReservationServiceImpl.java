package org.example.coworking.service;

import org.example.coworking.dao.ReservationDAO;
import org.example.coworking.dao.ReservationDAOImpl;
import org.example.coworking.model.Reservation;

import java.util.List;
import java.util.Optional;

public class ReservationServiceImpl implements ReservationService {
    ReservationDAO reservationDAO = new ReservationDAOImpl();

    @Override
    public void addReservation(Reservation reservation) {
        reservationDAO.addReservation(reservation);
    }

    @Override
    public void cancelReservation(int reservationId, int customerId, int coworkingId) {
        reservationDAO.cancelReservation(reservationId, customerId, coworkingId);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationDAO.getAllReservations();
    }

    @Override
    public List<Reservation> getReservationsByCustomer(int customerId) {
        return reservationDAO.getReservationsByCustomer(customerId);
    }

    @Override
    public Optional<Reservation> getReservationById(int reservationId) {
        return reservationDAO.getReservationById(reservationId);
    }
}
