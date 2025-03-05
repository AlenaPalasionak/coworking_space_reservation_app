package org.example.coworking.service;

import org.example.coworking.infrastructure.dao.ModelDao;
import org.example.coworking.infrastructure.dao.exception.CoworkingNotFoundException;
import org.example.coworking.infrastructure.dao.exception.ReservationNotFoundException;
import org.example.coworking.infrastructure.util.OccupationTimeChecker;
import org.example.coworking.infrastructure.util.ReservationTimeValidator;
import org.example.coworking.infrastructure.util.exception.InvalidTimeReservationException;
import org.example.coworking.model.*;
import org.example.coworking.service.exception.ForbiddenActionException;
import org.example.coworking.service.exception.TimeOverlapException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReservationServiceImpl implements ReservationService {
    ModelDao<Reservation, ReservationNotFoundException> reservationDao;
    ModelDao<CoworkingSpace, CoworkingNotFoundException> coworkingDao;

    public ReservationServiceImpl(ModelDao<Reservation, ReservationNotFoundException> reservationDao
            , ModelDao<CoworkingSpace, CoworkingNotFoundException> coworkingDao) {
        this.reservationDao = reservationDao;
        this.coworkingDao = coworkingDao;
    }

    @Override
    public void add(User customer, CoworkingSpace coworkingSpace, ReservationPeriod period)
            throws TimeOverlapException, InvalidTimeReservationException {

        ReservationTimeValidator.validateReservation(period.getStartTime(), period.getEndTime());
        if (OccupationTimeChecker.isTimeOverlapping(period, coworkingSpace)) {
            throw new TimeOverlapException(period);
        } else {
            reservationDao.add(new Reservation(customer, period, coworkingSpace));
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
    public List<Reservation> getAll(User user) {
        if (user != null && user.getClass() == Customer.class) {
            return reservationDao.getAll().stream()
                    .filter(reservation -> reservation.getCustomer().getId() == user.getId())
                    .collect(Collectors.toList());
        } else {
            return reservationDao.getAll();
        }
    }

    @Override
    public Optional<Reservation> getReservationByReservationId(int reservationId) throws ReservationNotFoundException {
        return reservationDao.getById(reservationId);
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
