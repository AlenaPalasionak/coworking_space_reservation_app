package org.example.coworking.service;

import org.example.coworking.model.User;
import org.example.coworking.infrastructure.dao.exception.UserNotFoundException;

import java.util.List;
import java.util.Optional;

public class AuthorizationServiceImpl implements AuthorizationService {
    private final UserService userService;

    public AuthorizationServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User authenticate(String name, String password, Class<? extends User> role) throws UserNotFoundException {

        List<User> users = userService.load();

        Optional<User> possibleUser = users.stream()
                .filter(user -> user.getName().equals(name))
                .filter(user -> user.getPassword().equals(password))
                .filter(role::isInstance)
                .findFirst();
        if (possibleUser.isPresent()) {
            return possibleUser.get();
        } else
            throw new UserNotFoundException(name);
    }
}

