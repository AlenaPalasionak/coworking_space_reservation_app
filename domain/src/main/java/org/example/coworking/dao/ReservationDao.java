package org.example.coworking.dao;

import org.example.coworking.dao.exception.ReservationNotFoundException;
import org.example.coworking.model.Reservation;

import java.util.List;

/**
 * This interface defines the data access operations for the {@link Reservation} model.
 * It extends the {@link ModelDao} interface to provide basic CRUD operations with
 * {@link ReservationNotFoundException} as the exception type for errors.
 * It provides additional methods for adding, deleting, retrieving, and saving reservation data.
 */
public interface ReservationDao extends ModelDao<Reservation, ReservationNotFoundException> {

    /**
     * Adds a new reservation to the data source.
     *
     * @param reservation the {@link Reservation} object to be added
     */
    void add(Reservation reservation);

    /**
     * Deletes a specified reservation from the data source.
     *
     * @param reservation the {@link Reservation} object to be deleted
     * @throws ReservationNotFoundException if the reservation cannot be found in the data source
     */
    void delete(Reservation reservation) throws ReservationNotFoundException;

    /**
     * Retrieves a reservation by its unique identifier.
     *
     * @param reservationId the ID of the reservation to be retrieved
     * @return the {@link Reservation} object with the specified ID
     * @throws ReservationNotFoundException if the reservation with the given ID is not found
     */
    Reservation getById(Long reservationId) throws ReservationNotFoundException;

    /**
     * Retrieves all reservations from the data source.
     *
     * @return a list of all {@link Reservation} objects
     */
    List<Reservation> getAll();

    /**
     * Loads the data from the data source. Typically used for initialization or data retrieval.
     */
    void load();

    /**
     * Saves the current state of the reservations to the data source.
     */
    void save();
}

