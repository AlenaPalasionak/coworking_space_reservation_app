package org.example.coworking.service;

import org.example.coworking.repository.exception.EntityNotFoundException;
import org.example.coworking.entity.User;

/**
 * This interface defines the operations for managing users in the system.
 * It provides a method to load users from a data source.
 */
public interface UserService {

    /**
     * Retrieves a user by their name, password, and role.
     *
     * @param name     the username
     * @param password the user's password
     * @param role the expected role of the user (e.g., Admin, Customer)
     * @return the authenticated {@code User} instance
     * @throws EntityNotFoundException if the user is not found or credentials are incorrect
     */
    <T extends User> T getUserByNamePasswordAndAndRole(String name, String password, Class<T> role) throws EntityNotFoundException;
    }

