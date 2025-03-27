package org.example.coworking.dao;

import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.model.CoworkingSpace;

import java.sql.Connection;

/**
 * This interface defines the data access operations for the {@link CoworkingSpace} model.
 * It extends the {@link Dao} interface to provide basic CRUD operations with
 * {@link EntityNotFoundException} as the exception type for errors.
 * It provides additional methods for adding, deleting, retrieving, and saving coworking space data.
 */
public interface CoworkingDao extends Dao<CoworkingSpace> {

    /**
     * Retrieves a coworking space by its ID.
     *
     * @param coworkingId The ID of the coworking space.
     * @param connection  The database connection to use.
     * @return The found {@link CoworkingSpace}.
     * @throws EntityNotFoundException if no coworking space is found with the given ID.
     */
    CoworkingSpace getById(Long coworkingId, Connection connection) throws EntityNotFoundException;

}
