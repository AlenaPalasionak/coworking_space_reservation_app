package org.example.coworking.service;

import org.example.coworking.infrastructure.dao.exception.CoworkingNotFoundException;
import org.example.coworking.infrastructure.dao.exception.ReservationNotFoundException;
import org.example.coworking.model.Reservation;
import org.example.coworking.model.User;
import org.example.coworking.service.exception.ForbiddenActionException;
import org.example.coworking.service.exception.InvalidTimeLogicException;
import org.example.coworking.service.exception.TimeOverlapException;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {
    void load();
    void save();
    void add(User customer, LocalDateTime startTime, LocalDateTime endTime, int coworkingSpaceId) throws TimeOverlapException, InvalidTimeLogicException, CoworkingNotFoundException;
    void delete(User user, int reservationId) throws ForbiddenActionException, ReservationNotFoundException;
    List<Reservation> getAllByUser(User user);
   Reservation getById(int reservationId) throws ReservationNotFoundException;
}
