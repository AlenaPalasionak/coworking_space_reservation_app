package org.example.coworking.service;

import org.example.coworking.infrastructure.dao.CoworkingDao;
import org.example.coworking.infrastructure.dao.CoworkingDaoImpl;
import org.example.coworking.infrastructure.dao.ReservationDao;
import org.example.coworking.infrastructure.dao.ReservationDaoImpl;
import org.example.coworking.infrastructure.util.OccupationTimeChecker;
import org.example.coworking.model.*;
import org.example.coworking.service.exception.ForbiddenActionException;
import org.example.coworking.service.exception.ReservationNotFoundException;
import org.example.coworking.service.exception.TimeOverlapException;

import java.util.List;
import java.util.Optional;

public class ReservationServiceImpl implements ReservationService {
    ReservationDao reservationDao;
    CoworkingDao coworkingDao;

    public ReservationServiceImpl() {
        this.reservationDao = new ReservationDaoImpl();
        this.coworkingDao = new CoworkingDaoImpl();
    }

    @Override
    public void add(User customer, CoworkingSpace coworkingSpace, ReservationPeriod period) throws TimeOverlapException {
        if (OccupationTimeChecker.isTimeOverlapping(period, coworkingSpace)) {
            throw new TimeOverlapException("This time period is occupied");
        } else {
            reservationDao.addReservation(new Reservation(customer, period, coworkingSpace));
        }
    }

    public void delete(Reservation reservation, User user, CoworkingSpace coworking) throws ForbiddenActionException {
        if (reservation.getCustomer().equals(user)) {
            reservationDao.delete(reservation);
        } else {
            throw new ForbiddenActionException("User " + user.getName() + " has no rights to delete this reservation");
        }
    }

    @Override
    public List<Reservation> getAllReservations(User user) {
        if (user != null && user.getClass() == Customer.class) {
            return reservationDao.getReservationsByCustomer(user.getId());
        } else {
            return reservationDao.getAllReservations();
        }
    }

    @Override
    public Optional<Reservation> getReservationByReservationId(int reservationId) throws ReservationNotFoundException {
        Optional<Reservation> possibleReservation = reservationDao.getReservationById(reservationId);
        if (possibleReservation.isEmpty()) {
            throw new ReservationNotFoundException("There are no reservations with id: " + reservationId);
        } else {
            return reservationDao.getReservationById(reservationId);
        }
    }

    @Override
    public void getReservationsFromJson() {
        reservationDao.getReservationsFromJson();
    }

    @Override
    public void saveToJSON() {
        reservationDao.saveToJSON();
    }
}
