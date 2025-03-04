package org.example.coworking.service;

import org.example.coworking.infrastructure.dao.CoworkingDao;
import org.example.coworking.infrastructure.dao.ReservationDao;
import org.example.coworking.infrastructure.dao.exception.ReservationNotFoundException;
import org.example.coworking.infrastructure.util.exception.InvalidTimeReservationException;
import org.example.coworking.infrastructure.util.OccupationTimeChecker;
import org.example.coworking.infrastructure.util.ReservationTimeValidator;
import org.example.coworking.model.*;
import org.example.coworking.service.exception.ForbiddenActionException;
import org.example.coworking.service.exception.TimeOverlapException;

import java.util.List;
import java.util.Optional;

public class ReservationServiceImpl implements ReservationService {
    ReservationDao reservationDao;
    CoworkingDao coworkingDao;

    public ReservationServiceImpl(ReservationDao reservationDao, CoworkingDao coworkingDao) {
        this.reservationDao = reservationDao;
        this.coworkingDao = coworkingDao;
    }

    @Override
    public void add(User customer, CoworkingSpace coworkingSpace, ReservationPeriod period) throws TimeOverlapException, InvalidTimeReservationException {
        ReservationTimeValidator.validateReservation(period.getStartTime(), period.getEndTime());
        if (OccupationTimeChecker.isTimeOverlapping(period, coworkingSpace)) {
            throw new TimeOverlapException(period);
        } else {
            reservationDao.addReservation(new Reservation(customer, period, coworkingSpace));
        }
    }

    public void delete(Reservation reservation, User user, CoworkingSpace coworking) throws ForbiddenActionException, ReservationNotFoundException {
        if (reservation.getCustomer().equals(user)) {
            reservationDao.delete(reservation);
        } else {
            throw new ForbiddenActionException(user.getClass());
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
        return reservationDao.getReservationById(reservationId);
    }

    @Override
    public void load() {
        reservationDao.load();
    }

    @Override
    public void save() {
        reservationDao.save();
    }
}
