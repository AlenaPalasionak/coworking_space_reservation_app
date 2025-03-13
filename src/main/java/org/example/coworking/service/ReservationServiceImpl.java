package org.example.coworking.service;

import org.example.coworking.infrastructure.dao.ReservationDao;
import org.example.coworking.infrastructure.dao.exception.CoworkingNotFoundException;
import org.example.coworking.infrastructure.dao.exception.ReservationNotFoundException;
import org.example.coworking.model.*;
import org.example.coworking.service.exception.ForbiddenActionException;
import org.example.coworking.service.exception.InvalidTimeLogicException;
import org.example.coworking.service.exception.TimeOverlapException;
import org.example.coworking.service.validator.OccupationTimeValidator;
import org.example.coworking.service.validator.TimeLogicValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationServiceImpl implements ReservationService {
    private final ReservationDao reservationDao;
    private final CoworkingService coworkingService;
    private final TimeLogicValidator timeLogicValidator;

    public ReservationServiceImpl(ReservationDao reservationDao, CoworkingService coworkingService, TimeLogicValidator timeLogicValidator) {
        this.reservationDao = reservationDao;
        this.coworkingService = coworkingService;
        this.timeLogicValidator = timeLogicValidator;
    }

    @Override
    public void load() {
        reservationDao.load();
    }

    @Override
    public void save() {
        reservationDao.save();
    }

    @Override
    public void add(User customer, LocalDateTime startTime, LocalDateTime endTime, int coworkingSpaceId) throws TimeOverlapException, InvalidTimeLogicException, CoworkingNotFoundException {
        ReservationPeriod period = new ReservationPeriod(startTime, endTime);
        CoworkingSpace coworkingSpace = coworkingService.getById(coworkingSpaceId);
        timeLogicValidator.validateReservation(startTime, endTime);
        if (OccupationTimeValidator.isTimeOverlapping(period, coworkingSpace)) {
            throw new TimeOverlapException(startTime + " - " + endTime + " overlaps with existing period");
        } else {
            Reservation reservation = new Reservation(customer, new ReservationPeriod(startTime, endTime), coworkingSpace);
            reservationDao.add(reservation);
        }
    }

    @Override
    public void delete(User user, int reservationId) throws ForbiddenActionException, ReservationNotFoundException {
        Reservation reservation = getById(reservationId);
        if (reservation.getCustomer().equals(user)) {
            reservationDao.delete(reservation);
        } else {
            throw new ForbiddenActionException("Action is forbidden for the user: " + user.getClass());
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
    public Reservation getById(int reservationId) throws ReservationNotFoundException {
        return reservationDao.getById(reservationId);
    }
}
