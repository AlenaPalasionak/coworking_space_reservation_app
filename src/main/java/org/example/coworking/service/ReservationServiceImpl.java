package org.example.coworking.service;

import org.example.coworking.infrastructure.dao.ReservationDao;
import org.example.coworking.infrastructure.dao.exception.CoworkingNotFoundException;
import org.example.coworking.infrastructure.dao.exception.ReservationNotFoundException;
import org.example.coworking.model.*;
import org.example.coworking.service.exception.ForbiddenActionException;
import org.example.coworking.service.exception.InvalidTimeLogicException;
import org.example.coworking.service.exception.TimeOverlapException;
import org.example.coworking.service.util.TimeLogicValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReservationServiceImpl implements ReservationService {
    private final ReservationDao reservationDao;
    private final CoworkingService coworkingService;

    public ReservationServiceImpl(ReservationDao reservationDao, CoworkingService coworkingService) {
        this.reservationDao = reservationDao;
        this.coworkingService = coworkingService;
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
        Optional<CoworkingSpace> possibleCoworkingSpace = coworkingService.getById(coworkingSpaceId);
        if (possibleCoworkingSpace.isPresent()) {
            CoworkingSpace coworkingSpace = possibleCoworkingSpace.get();
            TimeLogicValidator.validateReservation(startTime, endTime);
            if (OccupationTimeCheckerService.isTimeOverlapping(period, coworkingSpace)) {
                throw new TimeOverlapException(period);
            } else {
                Reservation reservation = new Reservation(customer, new ReservationPeriod(startTime, endTime), coworkingSpace);
                reservationDao.add(reservation);
            }
        }
    }

    @Override
    public void delete(User user, int reservationId) throws ForbiddenActionException, ReservationNotFoundException {
        Optional<Reservation> possibleReservation = getById(reservationId);
        if (possibleReservation.isEmpty()) {
            throw new ReservationNotFoundException(reservationId);
        }
        Reservation reservation = possibleReservation.get();
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
}
