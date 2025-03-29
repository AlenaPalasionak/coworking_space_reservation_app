package org.example.coworking.service;

import org.example.coworking.dao.ReservationDao;
import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.model.*;
import org.example.coworking.service.exception.ForbiddenActionException;
import org.example.coworking.service.exception.ReservationTimeException;
import org.example.coworking.service.exception.ServiceErrorCode;
import org.example.coworking.service.validator.OccupationTimeValidator;
import org.example.coworking.service.validator.TimeLogicValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
    public void add(User customer, LocalDateTime startTime, LocalDateTime endTime, Long coworkingSpaceId) throws ReservationTimeException, EntityNotFoundException {
        ReservationPeriod period = new ReservationPeriod(startTime, endTime);
        CoworkingSpace coworkingSpace = coworkingService.getById(coworkingSpaceId);
        timeLogicValidator.validateReservation(startTime, endTime);
        Set<ReservationPeriod> existingPeriodsOfACoworking = getAllReservationPeriodsByCoworking(coworkingSpaceId);
        if (OccupationTimeValidator.isTimeOverlapping(period, existingPeriodsOfACoworking)) {
            throw new ReservationTimeException(startTime + " - " + endTime + " overlaps with existing period", ServiceErrorCode.TIME_OVERLAPS);
        }
        Reservation reservation = new Reservation(customer, period, coworkingSpace);
        reservationDao.add(reservation);
    }

    @Override
    public void delete(User user, Long reservationId) throws ForbiddenActionException, EntityNotFoundException {
        Reservation reservation = getById(reservationId);
        if (reservation.getCustomer().getId().equals(user.getId())) {
            reservationDao.delete(reservation);
        } else {
            throw new ForbiddenActionException("Action is forbidden for the user: " + user.getName()
                    , ServiceErrorCode.FORBIDDEN_ACTION);
        }
    }

    @Override
    public List<Reservation> getAllByUser(User user) {
        if (user.getClass() == Customer.class) {
            return reservationDao.getAllReservationsByCustomer(user.getId());
        } else if (user.getClass() == Admin.class) {
            return reservationDao.getAllReservationsByAdmin(user.getId());
        } else throw new IllegalArgumentException("Unexpected user type: " + user.getClass().getSimpleName());
    }

    @Override
    public Reservation getById(Long reservationId) throws EntityNotFoundException {
        return reservationDao.getById(reservationId);
    }

    @Override
    public Set<ReservationPeriod> getAllReservationPeriodsByCoworking(Long coworkingId) {
        return reservationDao.getAllReservationPeriodsByCoworking(coworkingId);
    }
}
