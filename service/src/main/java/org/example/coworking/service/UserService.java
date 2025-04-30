package org.example.coworking.service;

import org.example.coworking.repository.exception.EntityNotFoundException;
import org.example.coworking.entity.User;

/**
 * This interface defines the operations for managing users in the system.
 * It provides a method to load users from a data source.
 */
public interface UserService {

    /**
     * Retrieves a user by their id.

     * @return the authenticated {@code User} instance
     * @throws EntityNotFoundException if the user is not found or credentials are incorrect
     */
    User findUserById(Long id) throws EntityNotFoundException;
    }

