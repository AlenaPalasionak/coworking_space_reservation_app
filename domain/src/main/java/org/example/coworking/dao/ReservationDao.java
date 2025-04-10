package org.example.coworking.dao;

import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.model.Reservation;

import java.util.List;
import java.util.Set;

/**
 * Data Access Object (DAO) interface for managing {@link Reservation} entities.
 */
public interface ReservationDao extends ModelDao<Reservation> {

    /**
     * Retrieves all reservation periods for a given coworking space.
     *
     * @param coworkingId the ID of the coworking space
     * @return a set of {@code ReservationPeriod} objects
     */
    Set<Reservation> getAllReservationsByCoworking(Long coworkingId);

    /**
     * Retrieves all reservations made by a specific customer.
     *
     * @param userId the ID of the customer
     * @return a list of {@code Reservation} objects
     */
    List<Reservation> getAllReservationsByCustomer(Long userId);

    /**
     * Retrieves all reservations managed by a specific admin.
     *
     * @param adminId the ID of the admin
     * @return a list of {@code Reservation} objects
     */
    List<Reservation> getAllReservationsByAdmin(Long adminId);

    /**
     * Retrieves all customer ID of a specific reservation.
     *
     * @param reservationId the ID of a reservation
     * @return a Long value of Customer ID
     */
    Long getCustomerIdByReservationId(Long reservationId) throws EntityNotFoundException;

}
