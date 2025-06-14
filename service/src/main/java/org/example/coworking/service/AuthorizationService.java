package org.example.coworking.service;

import org.example.coworking.repository.exception.EntityNotFoundException;
import org.example.coworking.model.User;

/**
 * This interface defines the authentication operations for users.
 * It provides a method for authenticating a user based on their name and password
 * while associating them with a specific role class.
 */
public interface AuthorizationService {

    /**
     * Authenticates a User with the given name and password, and associates them with a role class.
     *
     * @param name     the name of the User to authenticate
     * @param password the password of the User to authenticate
     * @param role     the role class associated with the User
     * @return the authenticated {@link User} object
     * @throws EntityNotFoundException if no User matching the provided name and password is found
     */
    <T extends User> T authenticate(String name, String password, Class<T> role) throws EntityNotFoundException;

}

