package org.example.coworking.dao;

import org.example.coworking.model.Reservation;
import org.example.coworking.model.ReservationPeriod;

import java.util.List;
import java.util.Set;

/**
 * Data Access Object (DAO) interface for managing {@link Reservation} entities.
 */
public interface ReservationDao extends Dao<Reservation> {

    Set<ReservationPeriod> getAllReservationPeriodsByCoworking(Long coworkingId);
    List<Reservation> getAllReservationsByCustomer(Long userId);
    List<Reservation> getAllReservationsByAdmin(Long adminId);
}


