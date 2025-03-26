package org.example.coworking.dao;

import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.model.User;

import java.sql.Connection;

/**
 * This interface defines the data access operations for the {@link User} model.
 * It provides a method for loading a list of users from the data storage.
 */
public interface UserDao {

    /**
     * Finds a user by name, password, and role.
     *
     * @param name      the username
     * @param password  the password
     * @param roleClass the user role class
     * @return the found {@link User}
     * @throws EntityNotFoundException if no user matches the criteria
     */
    User getUserByNamePasswordAndRole(String name, String password, Class<? extends User> roleClass)
            throws EntityNotFoundException;

    /**
     * Finds a user by ID using a database connection.
     *
     * @param id         the user ID
     * @param connection the database connection
     * @return the found {@link User}, or {@code null} if not found
     */
    User getById(Long id, Connection connection);

    /**
     * Finds a user by ID.
     *
     * @param id the user ID
     * @return the found {@link User}, or {@code null} if not found
     */
    User getById(Long id);
}
