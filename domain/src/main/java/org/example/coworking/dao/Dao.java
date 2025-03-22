package org.example.coworking.dao;

import java.util.List;

/**
 * This interface defines the basic data access operations for entities (model).
 * It provides methods for adding, deleting, retrieving by ID, and fetching all instances of a model.
 * Additionally, it defines methods for loading and saving data from storage.
 *
 * @param <T> the type of the model object to be managed by the DAO
 * @param <E> the type of exception that may be thrown by operations in this interface
 */
public interface Dao<T, E extends Exception> {

    /**
     * Adds a new model object to the data source.
     *
     * @param object the model object to be added
     */
    void add(T object);

    /**
     * Deletes the specified model object from the data source.
     *
     * @param object the model object to be deleted
     * @throws E if an error occurs during the delete operation
     */
    void delete(T object) throws E;

    /**
     * Retrieves a model object by its unique identifier.
     *
     * @param id the ID of the model object to be retrieved
     * @return the model object with the specified ID
     * @throws E if an error occurs while retrieving the object
     */
    T getById(Long id) throws E;

    /**
     * Retrieves all model objects from the data source.
     *
     * @return a list of all model objects
     */
    List<T> getAll();
}

