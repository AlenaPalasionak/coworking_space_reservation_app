package org.example.coworking.service;

import org.example.coworking.model.User;
import org.example.coworking.service.exception.UserNotFoundException;

import java.util.Optional;

public interface AuthorizationService {
    Optional<User> authenticate(String name, String password, Class<? extends User> roleClass) throws UserNotFoundException;
}
