package org.example.coworking.service;

import org.example.coworking.model.User;

import java.util.List;

/**
 * This interface defines the operations for managing users in the system.
 * It provides a method to load users from a data source.
 */
public interface UserService {

    /**
     * Loads the list of users from a data source.
     *
     * @return a list of {@link User} objects representing all users loaded from the data source
     */
    List<User> load();
}

