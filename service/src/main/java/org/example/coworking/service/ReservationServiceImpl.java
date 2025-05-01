package org.example.coworking.service;

import jakarta.persistence.PersistenceException;
import org.example.coworking.entity.Reservation;
import org.example.coworking.entity.User;
import org.example.coworking.repository.ReservationRepository;
import org.example.coworking.repository.exception.CustomDataExcessException;
import org.example.coworking.repository.exception.DaoErrorCode;
import org.example.coworking.repository.exception.EntityNotFoundException;
import org.example.coworking.service.exception.ForbiddenActionException;
import org.example.coworking.service.exception.ReservationTimeException;
import org.example.coworking.service.exception.ServiceErrorCode;
import org.example.coworking.service.validator.OccupationTimeValidator;
import org.example.coworking.service.validator.TimeLogicValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.example.coworking.logger.Log.TECHNICAL_LOGGER;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final TimeLogicValidator timeLogicValidator;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository,
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
        try {
            reservationRepository.save(reservation);
        } catch (DataAccessException e) {
            TECHNICAL_LOGGER.error("Database error occurred while creating a new Reservation: {}", reservation, e);
            throw new CustomDataExcessException(String.format("Database error while creating a new Reservation:: %s",
                    reservation), e);
        }
    }

    @Override
    public void delete(User customer, Long reservationId) throws ForbiddenActionException, EntityNotFoundException {
        Reservation reservation = findById(reservationId);
        if (!reservation.getCustomer().getId().equals(customer.getId())) {
            throw new ForbiddenActionException(String.format("Action is forbidden for the user: %s", customer.getId()),
                    ServiceErrorCode.FORBIDDEN_ACTION);
        } else {
            try {
                reservationRepository.delete(reservation);
            } catch (DataAccessException e) {
                TECHNICAL_LOGGER.error("Database error occurred while deleting Reservation with ID: {}.",
                        reservationId, e);
                throw new CustomDataExcessException(String.format("Database error occurred while deleting Reservation with ID: %d.",
                        reservationId), e);
            }
        }
    }

    @Override
    public Reservation findById(Long reservationId) throws EntityNotFoundException {
        Optional<Reservation> possibleReservation;
        try {
            possibleReservation = reservationRepository.findById(reservationId);
        } catch (DataAccessException e) {
            TECHNICAL_LOGGER.error("Database error while getting Reservation by ID: {}", reservationId, e);
            throw new CustomDataExcessException(String.format("Database error while getting Reservation by ID: %d",
                    reservationId), e);
        }
        if (possibleReservation.isEmpty()) {
            throw new EntityNotFoundException(String.format("Failure to get Reservation with ID: %d",
                    reservationId), DaoErrorCode.COWORKING_IS_NOT_FOUND);
        }

        return possibleReservation.get();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Reservation> getAllReservationsByCoworking(Long coworkingSpaceId) {
        try {
            return reservationRepository.getAllReservationsByCoworking(coworkingSpaceId);
        } catch (DataAccessException e) {
            TECHNICAL_LOGGER.error("Database error occurred while getting reservations by Coworking Space ID: {}.",
                    coworkingSpaceId, e);
            throw new CustomDataExcessException(String.format("Database error occurred while getting reservations by Coworking Space ID: %d.",
                    coworkingSpaceId), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getAllReservationsByCustomer(User customer) {
        Long customerId = customer.getId();

        try {
            return reservationRepository.getAllReservationsByCustomer(customerId);
        } catch (DataAccessException e) {
            TECHNICAL_LOGGER.error("Database error occurred while getting reservations by customer ID: {}.",
                    customerId, e);
            throw new CustomDataExcessException(String.format("Database error occurred while getting reservations by customer ID: %d.",
                    customerId), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getAllReservationsByAdmin(User admin) {
        Long adminId = admin.getId();
        try {
            return reservationRepository.getAllReservationsByAdmin(adminId);
        } catch (PersistenceException e) {
            TECHNICAL_LOGGER.error("Database error occurred while getting reservations by customer ID: {}.",
                    adminId, e);
            throw new CustomDataExcessException(String.format("Database error occurred while getting reservations by customer ID: %d.",
                    adminId), e);
        }
    }
}
