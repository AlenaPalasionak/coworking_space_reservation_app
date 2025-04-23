package org.example.coworking.repository;

import org.example.coworking.repository.exception.EntityNotFoundException;
import org.example.coworking.entity.User;

/**
 * This interface defines the data access operations for the {@link User} model.
 * It provides a method for loading a list of users from the data storage.
 */
public interface UserRepository {

    /**
     * Finds a user by name, password, and role.
     *
     * @param name      the username
     * @param password  the password
     * @param role the user role class
     * @return the found {@link User}
     * @throws EntityNotFoundException if no user matches the criteria
     */
    <T extends User> T getUserByNamePasswordAndRole(String name, String password, Class<T> role) throws EntityNotFoundException;

}
