package org.example.coworking.service;

import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.model.User;

/**
 * This interface defines the operations for managing users in the system.
 * It provides a method to load users from a data source.
 */
public interface UserService {
    User getUserByNamePasswordAndAndRole(String name, String password, Class<? extends User> roleClass) throws EntityNotFoundException;

}

