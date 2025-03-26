package org.example.coworking.dao;

import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.model.User;

import java.sql.Connection;

/**
 * This interface defines the data access operations for the {@link User} model.
 * It provides a method for loading a list of users from the data storage.
 */
public interface UserDao extends Dao<User> {
    User getUserByNamePasswordAndRole(String name, String password, Class<? extends User> roleClass) throws EntityNotFoundException;

    User getById(Long id, Connection connection);

}
