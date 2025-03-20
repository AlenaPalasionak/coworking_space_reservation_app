package org.example.coworking.service;

import org.example.coworking.model.User;
import org.example.coworking.dao.exception.UserNotFoundException;

/**
 * This interface defines the authentication operations for users.
 * It provides a method for authenticating a user based on their name and password
 * while associating them with a specific role class.
 */
public interface AuthorizationService {

    /**
     * Authenticates a user with the given name and password, and associates them with a role class.
     *
     * @param name the name of the user to authenticate
     * @param password the password of the user to authenticate
     * @param roleClass the role class associated with the user
     * @return the authenticated {@link User} object
     * @throws UserNotFoundException if no user matching the provided name and password is found
     */
    User authenticate(String name, String password, Class<? extends User> roleClass) throws UserNotFoundException;
}

