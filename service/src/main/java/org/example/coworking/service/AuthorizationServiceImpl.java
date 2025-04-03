package org.example.coworking.service;

import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.model.User;

/**
 * Implementation of the {@link AuthorizationService} interface.
 * Handles user authentication.
 */
public class AuthorizationServiceImpl implements AuthorizationService {
    private final UserService userService;

    /**
     * Constructs an {@code AuthorizationServiceImpl} with the specified {@code UserService}.
     *
     * @param userService the user service to retrieve user authentication data
     */
    public AuthorizationServiceImpl(UserService userService) {
        this.userService = userService;
    }

    /**
     * Authenticates a user based on name, password, and role.
     *
     * @param name     the username
     * @param password the user's password
     * @param role     the expected role of the user (e.g., Admin, Customer)
     * @return the authenticated {@code User} instance
     * @throws EntityNotFoundException if the user is not found or credentials are invalid
     */
    @Override
    public User authenticate(String name, String password, Class<? extends User> role) throws EntityNotFoundException {
        return userService.getUserByNamePasswordAndAndRole(name, password, role);
    }
}

