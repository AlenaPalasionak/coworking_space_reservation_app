package org.example.coworking.service;

import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.model.User;

/**
 * This interface defines the operations for managing users in the system.
 * It provides a method to load users from a data source.
 */
public interface UserService {

    /**
     * Retrieves a user by their name, password, and role.
     *
     * @param name      the username
     * @param password  the user's password
     * @param roleClass the expected role of the user (e.g., Admin, Customer)
     * @return the authenticated {@code User} instance
     * @throws EntityNotFoundException if the user is not found or credentials are incorrect
     */
    User getUserByNamePasswordAndAndRole(String name, String password, Class<? extends User> roleClass) throws EntityNotFoundException;

}

