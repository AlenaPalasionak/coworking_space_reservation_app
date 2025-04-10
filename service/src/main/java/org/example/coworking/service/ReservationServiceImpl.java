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
        CoworkingSpace coworkingSpace = coworkingService.getById(coworkingSpaceId);
        timeLogicValidator.validateReservation(startTime, endTime);
        Set<Reservation> existingReservationsOfACoworking = getAllReservationsByCoworking(coworkingSpaceId);
        if (OccupationTimeValidator.isTimeOverlapping(startTime, endTime, existingReservationsOfACoworking)) {
            throw new ReservationTimeException(String.format("%s - %s overlaps with existing period", startTime, endTime),
                    ServiceErrorCode.TIME_OVERLAPS);
        }
        Reservation reservation = new Reservation(customer, startTime, endTime, coworkingSpace);
        reservationDao.create(reservation);
    }

    @Override
    public void delete(User user, Long reservationId) throws ForbiddenActionException, EntityNotFoundException {
        Reservation reservation = reservationDao.getById(reservationId);
        if (reservation.getCustomer().getId().equals(user.getId())) {
            reservationDao.delete(reservation);
        } else {
            throw new ForbiddenActionException(String.format("Action is forbidden for the user: %s", user.getId())
                    , ServiceErrorCode.FORBIDDEN_ACTION);
        }
    }

    @Override
    public List<Reservation> getAllByUser(User user) {//2
        if (user.getClass() == Customer.class) {
            return reservationDao.getAllReservationsByCustomer(user.getId());
        } else if (user.getClass() == Admin.class) {
            return reservationDao.getAllReservationsByAdmin(user.getId());
        } else
            throw new IllegalArgumentException(String.format("Unexpected user type: %s", user.getClass().getSimpleName()));
    }

    @Override
    public Reservation getById(Long reservationId) throws EntityNotFoundException {
        return reservationDao.getById(reservationId);
    }

    @Override
    public Set<Reservation> getAllReservationsByCoworking(Long coworkingId) {
        return reservationDao.getAllReservationsByCoworking(coworkingId);
    }

}
