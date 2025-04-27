package org.example.coworking.service;

import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link AuthorizationService} interface.
 * Handles user authentication.
 */
@Service
public class AuthorizationServiceImpl implements AuthorizationService {
    private final UserService userService;

    /**
     * Constructs an {@code AuthorizationServiceImpl} with the specified {@code UserService}.
     *
     * @param userService the user service to retrieve user authentication data
     */
    @Autowired
    public AuthorizationServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public <T extends User> T authenticate(String name, String password, Class<T> role) throws EntityNotFoundException {
        return userService.getUserByNamePasswordAndAndRole(name, password, role);
    }
}
