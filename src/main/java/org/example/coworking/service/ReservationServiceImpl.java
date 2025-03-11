package org.example.coworking.service;

import org.example.coworking.infrastructure.dao.ReservationDao;
import org.example.coworking.infrastructure.dao.exception.ReservationNotFoundException;
import org.example.coworking.service.util.TimeLogicValidator;
import org.example.coworking.service.exception.InvalidTimeLogicException;
import org.example.coworking.model.*;
import org.example.coworking.service.exception.ForbiddenActionException;
import org.example.coworking.service.exception.TimeOverlapException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReservationServiceImpl implements ReservationService {
    private final ReservationDao reservationDao;

    public ReservationServiceImpl(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    @Override
    public void add(LocalDateTime startTime, LocalDateTime endTime, User customer, CoworkingSpace coworkingSpace)
            throws TimeOverlapException, InvalidTimeLogicException {
        ReservationPeriod period = new ReservationPeriod(startTime, endTime);

        TimeLogicValidator.validateReservation(startTime,endTime);
        if (OccupationTimeCheckerService.isTimeOverlapping(period, coworkingSpace)) {
            throw new TimeOverlapException(period);
        } else {
            reservationDao.add(new Reservation(customer, period, coworkingSpace));
        }
    }

    @Override
    public void delete(User user, Reservation reservation) throws ForbiddenActionException, ReservationNotFoundException {
        if (reservation.getCustomer().equals(user)) {
            reservationDao.delete(reservation);
        } else {
            throw new ForbiddenActionException(user.getClass());
        }
    }

    @Override
    public List<Reservation> getAllByUser(User user) {
        if (user != null && user.getClass() == Customer.class) {
            return reservationDao.getAll().stream()
                    .filter(reservation -> reservation.getCustomer().getId() == user.getId())
                    .collect(Collectors.toList());
        } else {
            return reservationDao.getAll();
        }
    }

    @Override
    public Optional<Reservation> getById(int reservationId) throws ReservationNotFoundException {
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
