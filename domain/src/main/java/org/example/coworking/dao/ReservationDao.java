package org.example.coworking.dao;

import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.model.Reservation;

import java.sql.Connection;

/**
 * Data Access Object (DAO) interface for managing {@link Reservation} entities.
 */
public interface ReservationDao extends Dao<Reservation> {

    /**
     * Retrieves a reservation by its ID using the provided database connection.
     *
     * @param reservationId the ID of the reservation to retrieve
     * @param connection the database connection to use for the query
     * @return the found {@link Reservation} entity
     * @throws EntityNotFoundException if no reservation is found with the given ID
     */
    Reservation getById(Long reservationId, Connection connection) throws EntityNotFoundException;
}


