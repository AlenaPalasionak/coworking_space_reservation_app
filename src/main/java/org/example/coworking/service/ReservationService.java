package org.example.coworking.service;

import org.example.coworking.model.CoworkingSpace;
import org.example.coworking.model.Reservation;
import org.example.coworking.model.ReservationPeriod;
import org.example.coworking.model.User;
import org.example.coworking.service.exception.ForbiddenActionException;
import org.example.coworking.service.exception.ReservationNotFoundException;
import org.example.coworking.service.exception.TimeOverlapException;

import java.util.List;
import java.util.Optional;

public interface ReservationService {
    void add(User customer, CoworkingSpace coworking, ReservationPeriod period) throws TimeOverlapException;
    void delete(Reservation reservation, User user, CoworkingSpace coworking) throws ForbiddenActionException;
    List<Reservation> getAllReservations(User user);
    Optional<Reservation> getReservationByReservationId(int reservationId) throws ReservationNotFoundException;
}
