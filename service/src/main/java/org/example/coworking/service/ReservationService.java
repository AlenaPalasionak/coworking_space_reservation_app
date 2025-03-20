package org.example.coworking.service;

import org.example.coworking.dao.exception.CoworkingNotFoundException;
import org.example.coworking.dao.exception.ReservationNotFoundException;
import org.example.coworking.model.Reservation;
import org.example.coworking.model.User;
import org.example.coworking.service.exception.ForbiddenActionException;
import org.example.coworking.service.exception.ReservationTimeException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This interface defines the operations for managing reservations in the system.
 * It provides methods to load, save, add, delete, and retrieve reservations for users.
 */
public interface ReservationService {

    /**
     * Loads the reservations from a data source.
     */
    void load();

    /**
     * Saves the current state of reservations to a data source.
     */
    void save();

    /**
     * Adds a new reservation for the specified customer with the given start and end times
     * and the associated coworking space ID.
     *
     * @param customer the user who is making the reservation
     * @param startTime the start time of the reservation
     * @param endTime the end time of the reservation
     * @param coworkingSpaceId the ID of the coworking space being reserved
     * @throws ReservationTimeException if the reservation times are invalid (e.g., overlapping with existing reservations)
     * @throws CoworkingNotFoundException if the coworking space with the given ID is not found
     */
    void add(User customer, LocalDateTime startTime, LocalDateTime endTime, Long coworkingSpaceId)
            throws ReservationTimeException, CoworkingNotFoundException;

    /**
     * Deletes an existing reservation made by the specified user.
     *
     * @param user the user who is deleting the reservation
     * @param reservationId the ID of the reservation to be deleted
     * @throws ForbiddenActionException if the user is not allowed to delete the reservation
     * @throws ReservationNotFoundException if the reservation with the given ID is not found
     */
    void delete(User user, Long reservationId) throws ForbiddenActionException, ReservationNotFoundException;

    /**
     * Retrieves all reservations associated with the specified user.
     *
     * @param user the user whose reservations are to be retrieved
     * @return a list of {@link Reservation} objects associated with the user
     */
    List<Reservation> getAllByUser(User user);

    /**
     * Retrieves a reservation by its ID.
     *
     * @param reservationId the ID of the reservation to be retrieved
     * @return the {@link Reservation} object associated with the given ID
     * @throws ReservationNotFoundException if no reservation with the given ID is found
     */
    Reservation getById(Long reservationId) throws ReservationNotFoundException;
}
