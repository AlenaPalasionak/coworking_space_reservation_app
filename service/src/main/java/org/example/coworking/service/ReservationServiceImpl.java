package org.example.coworking.service;

import org.example.coworking.entity.Admin;
import org.example.coworking.entity.Customer;
import org.example.coworking.entity.Reservation;
import org.example.coworking.repository.ReservationRepository;
import org.example.coworking.repository.exception.EntityNotFoundException;
import org.example.coworking.service.exception.ForbiddenActionException;
import org.example.coworking.service.exception.ReservationTimeException;
import org.example.coworking.service.exception.ServiceErrorCode;
import org.example.coworking.service.validator.OccupationTimeValidator;
import org.example.coworking.service.validator.TimeLogicValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final TimeLogicValidator timeLogicValidator;

    @Autowired
    public ReservationServiceImpl(@Qualifier("jpaReservationRepository") ReservationRepository reservationRepository,
                                  TimeLogicValidator timeLogicValidator) {
        this.reservationRepository = reservationRepository;
        this.timeLogicValidator = timeLogicValidator;
    }

    @Override
    public void add(Reservation reservation) throws ReservationTimeException, EntityNotFoundException {
        LocalDateTime startTime = reservation.getStartTime();
        LocalDateTime endTime = reservation.getEndTime();
        Long coworkingSpaceId = reservation.getCoworkingSpace().getId();
        timeLogicValidator.validateReservation(startTime, endTime);
        Set<Reservation> existingReservationsOfACoworking = getAllReservationsByCoworking(coworkingSpaceId);
        if (OccupationTimeValidator.isTimeOverlapping(startTime, endTime, existingReservationsOfACoworking)) {
            throw new ReservationTimeException(String.format("%s - %s overlaps with existing period", startTime, endTime),
                    ServiceErrorCode.TIME_OVERLAPS);
        }
        reservationRepository.create(reservation);
    }

    @Override
    public void delete(Customer customer, Long reservationId) throws ForbiddenActionException, EntityNotFoundException {
        Reservation reservation = getById(reservationId);
        if (reservation.getCustomer().getId().equals(customer.getId())) {
            reservationRepository.delete(reservation);
        } else {
            throw new ForbiddenActionException(String.format("Action is forbidden for the user: %s", customer.getId()),
                    ServiceErrorCode.FORBIDDEN_ACTION);
        }
    }

    @Override
    public Reservation getById(Long reservationId) throws EntityNotFoundException {
        return reservationRepository.getById(reservationId);
    }

    @Override
    public List<Reservation> getAllReservationsByCustomer(Customer customer) {
        return reservationRepository.getAllReservationsByCustomer(customer.getId());
    }

    @Override
    public List<Reservation> getAllReservationsByAdmin(Admin admin) {
        return reservationRepository.getAllReservationsByAdmin(admin.getId());
    }

    @Override
    public Set<Reservation> getAllReservationsByCoworking(Long coworkingId) {
        return reservationRepository.getAllReservationsByCoworking(coworkingId);
    }
}
