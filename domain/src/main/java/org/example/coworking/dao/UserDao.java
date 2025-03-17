package org.example.coworking.dao;

import org.example.coworking.model.User;

import java.util.List;

/**
 * This interface defines the data access operations for the {@link User} model.
 * It provides a method for loading a list of users from the data storage.
 */
public interface UserDao {

    /**
     * Loads all the users from the data storage.
     *
     * @return a list of all {@link User} objects
     */
    List<User> load();
}
