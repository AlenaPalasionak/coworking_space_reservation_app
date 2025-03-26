package org.example.coworking.service;

import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.model.User;

public class AuthorizationServiceImpl implements AuthorizationService {
    private final UserService userService;

    public AuthorizationServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User authenticate(String name, String password, Class<? extends User> role) throws EntityNotFoundException {
        return userService.getUserByNamePasswordAndAndRole(name, password, role);
    }
}

