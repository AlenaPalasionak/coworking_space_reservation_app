package org.example.coworking.service;

import org.example.coworking.infrastructure.dao.exception.ReservationNotFoundException;
import org.example.coworking.infrastructure.util.exception.InvalidTimeReservationException;
import org.example.coworking.model.CoworkingSpace;
import org.example.coworking.model.Reservation;
import org.example.coworking.model.ReservationPeriod;
import org.example.coworking.model.User;
import org.example.coworking.service.exception.ForbiddenActionException;
import org.example.coworking.service.exception.TimeOverlapException;

import java.util.List;
import java.util.Optional;

public interface ReservationService {
    void add(User customer, CoworkingSpace coworkingSpace, ReservationPeriod period) throws TimeOverlapException, InvalidTimeReservationException;
    void delete(User user, Reservation reservation) throws ForbiddenActionException, ReservationNotFoundException;
    List<Reservation> getAllByUser(User user);
    Optional<Reservation> getById(int reservationId) throws ReservationNotFoundException;
    void load();
    void save();
}
