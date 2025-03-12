package org.example.coworking.service;

import org.example.coworking.model.User;
import org.example.coworking.service.exception.UserNotFoundException;

public interface AuthorizationService {
    User authenticate(String name, String password, Class<? extends User> roleClass) throws UserNotFoundException;
}
