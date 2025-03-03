package org.example.coworking.service;

import org.example.coworking.model.User;
import org.example.coworking.service.exception.UserNotFoundException;

import java.util.List;
import java.util.Optional;

public class AuthorizationServiceImpl implements AuthorizationService {
    UserService userService;

    public AuthorizationServiceImpl() {
        this.userService = new UserServiceImpl();
    }

    @Override
    public Optional<User> authenticate(String name, String password, Class<? extends User> role) throws UserNotFoundException {

        List<User> users = userService.getUsersFromJson();

        Optional<User> possibleUser = users.stream()
                .filter(user -> user.getName().equals(name))
                .filter(user -> user.getPassword().equals(password))
                .filter(user -> role.isInstance(user)).findFirst();

        if (possibleUser.isEmpty()) {
            throw new UserNotFoundException("User with name " + name + " and role " +
                    role.getSimpleName() + " not found");
        }

        return possibleUser;
    }
}
