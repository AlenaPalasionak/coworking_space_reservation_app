package org.example.coworking.service;

import org.example.coworking.repository.exception.EntityNotFoundException;
import org.example.coworking.entity.Admin;
import org.example.coworking.entity.Customer;
import org.example.coworking.entity.Reservation;
import org.example.coworking.service.exception.ForbiddenActionException;
import org.example.coworking.service.exception.ReservationTimeException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * This interface defines the operations for managing reservations in the system.
 * It provides methods to load, save, add, delete, and retrieve reservations for users.
 */
public interface ReservationService {

    /**
     * Adds a new reservation for the specified customer with the given start and end times
     * and the associated coworking space ID.
     *
     * @param customer         the user who is making the reservation
     * @param startTime        the start time of the reservation
     * @param endTime          the end time of the reservation
     * @param coworkingSpaceId the ID of the coworking space being reserved
     * @throws ReservationTimeException if the reservation times are invalid (e.g., overlapping with existing reservations)
     * @throws EntityNotFoundException  if the coworking space with the given ID is not found
     */
    void add(Customer customer, LocalDateTime startTime, LocalDateTime endTime, Long coworkingSpaceId)
            throws ReservationTimeException, EntityNotFoundException;

    /**
     * Deletes an existing reservation made by the specified user.
     *
     * @param customer      the user who is deleting the reservation
     * @param reservationId the ID of the reservation to be deleted
     * @throws ForbiddenActionException if the user is not allowed to delete the reservation
     * @throws EntityNotFoundException  if the reservation with the given ID is not found
     */
    void delete(Customer customer, Long reservationId) throws ForbiddenActionException, EntityNotFoundException;

    /**
     * Retrieves a reservation by its ID.
     *
     * @param reservationId the ID of the reservation to be retrieved
     * @return the {@link Reservation} object associated with the given ID
     * @throws EntityNotFoundException if no reservation with the given ID is found
     */
    Reservation getById(Long reservationId) throws EntityNotFoundException;

    /**
     * Retrieves all reservations associated with the specified user.
     *
     * @param customer the user whose reservations are to be retrieved
     * @return a list of {@link Reservation} objects associated with the user
     */
    List<Reservation> getAllReservationsByCustomer(Customer customer);

    /**
     * Retrieves all reservations associated with the specified user.
     *
     * @param admin the user whose reservations are to be retrieved
     * @return a list of {@link Reservation} objects associated with the user
     */

    List<Reservation> getAllReservationsByAdmin(Admin admin);


    /**
     * Retrieves all reservation periods for a specific coworking space.
     *
     * @param coworkingId the ID of the coworking space
     * @return a set of {@code ReservationPeriod} objects representing booked time slots
     */
    Set<Reservation> getAllReservationsByCoworking(Long coworkingId);
}
