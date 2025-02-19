package org.example.coworking.service;

import org.example.coworking.dao.ReservationDAO;
import org.example.coworking.dao.ReservationDAOImpl;
import org.example.coworking.model.Reservation;

import java.util.List;

public class ReservationServiceImpl implements ReservationService {
    ReservationDAO reservationDAO = new ReservationDAOImpl();

    @Override
    public void addReservation(Reservation reservation) {
        reservationDAO.addReservation(reservation);
    }

    @Override
    public void cancelReservation(int id) {
        reservationDAO.cancelReservation(id);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationDAO.getAllReservations();
    }

    @Override
    public List<Reservation> getReservationsByCustomer(int customerId) {
        return reservationDAO.getReservationsByCustomer(customerId);
    }
}
