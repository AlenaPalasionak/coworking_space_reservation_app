package org.example.coworking.service;

import org.example.coworking.entity.Reservation;
import org.example.coworking.entity.User;
import org.example.coworking.repository.exception.EntityNotFoundException;
import org.example.coworking.service.exception.ForbiddenActionException;
import org.example.coworking.service.exception.ReservationTimeException;

import java.util.List;
import java.util.Set;

/**
 * This interface defines the operations for managing reservations in the system.
 * It provides methods to load, save, add, delete, and retrieve reservations for users.
 */
public interface ReservationService {

    /**
     * Adds a new reservation
     *
     * @throws ReservationTimeException if the reservation times are invalid (e.g., overlapping with existing reservations)
     * @throws EntityNotFoundException  if the coworking space with the given ID is not found
     */
    void add(Reservation reservation) throws ReservationTimeException, EntityNotFoundException;

    /**
     * Deletes an existing reservation made by the specified user.
     *
     * @param customer      the user who is deleting the reservation
     * @param reservationId the ID of the reservation to be deleted
     * @throws ForbiddenActionException if the user is not allowed to delete the reservation
     * @throws EntityNotFoundException  if the reservation with the given ID is not found
     */
    void delete(User customer, Long reservationId) throws ForbiddenActionException, EntityNotFoundException;

    /**
     * Retrieves a reservation by its ID.
     *
     * @param reservationId the ID of the reservation to be retrieved
     * @return the {@link Reservation} object associated with the given ID
     * @throws EntityNotFoundException if no reservation with the given ID is found
     */
    Reservation findById(Long reservationId) throws EntityNotFoundException;

    /**
     * Retrieves all reservations associated with the specified user.
     *
     * @param customer the user whose reservations are to be retrieved
     * @return a list of {@link Reservation} objects associated with the user
     */
    List<Reservation> getAllReservationsByCustomer(User customer);

    /**
     * Retrieves all reservations associated with the specified user.
     *
     * @param admin the user whose reservations are to be retrieved
     * @return a list of {@link Reservation} objects associated with the user
     */

    List<Reservation> getAllReservationsByAdmin(User admin);

    /**
     * Retrieves all reservation periods for a specific coworking space.
     *
     * @param coworkingId the ID of the coworking space
     * @return a set of {@code ReservationPeriod} objects representing booked time slots
     */
    Set<Reservation> getAllReservationsByCoworking(Long coworkingId);
}
