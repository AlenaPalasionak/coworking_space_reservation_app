package org.example.coworking.dao;

import org.example.coworking.dao.exception.CoworkingNotFoundException;
import org.example.coworking.model.CoworkingSpace;

import java.util.List;
/**
 * This interface defines the data access operations for the {@link CoworkingSpace} model.
 * It extends the {@link Dao} interface to provide basic CRUD operations with
 * {@link CoworkingNotFoundException} as the exception type for errors.
 * It provides additional methods for adding, deleting, retrieving, and saving coworking space data.
 */
public interface CoworkingDao extends Dao<CoworkingSpace, CoworkingNotFoundException> {

    /**
     * Adds a new coworking space to the data source.
     *
     * @param coworkingSpace the {@link CoworkingSpace} object to be added
     */
    @Override
    void add(CoworkingSpace coworkingSpace);

    /**
     * Deletes a specified coworking space from the data source.
     *
     * @param coworking the {@link CoworkingSpace} object to be deleted
     * @throws CoworkingNotFoundException if the coworking space cannot be found in the data source
     */
    @Override
    void delete(CoworkingSpace coworking) throws CoworkingNotFoundException;

    /**
     * Retrieves a coworking space by its unique identifier.
     *
     * @param id the ID of the coworking space to be retrieved
     * @return the {@link CoworkingSpace} object with the specified ID
     * @throws CoworkingNotFoundException if the coworking space with the given ID is not found
     */
    @Override
    CoworkingSpace getById(Long id) throws CoworkingNotFoundException;

    /**
     * Retrieves all coworking spaces from the data source.
     *
     * @return a list of all {@link CoworkingSpace} objects
     */
    @Override
    List<CoworkingSpace> getAll();

    /**
     * Loads the data from the data source. Typically used for initialization or data retrieval.
     */
    void load();

    /**
     * Saves the current state of the coworking spaces to the data source.
     */
    void save();
}
