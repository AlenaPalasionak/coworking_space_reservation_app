package org.example.coworking.dao;

import org.example.coworking.model.User;

import java.sql.Connection;

public class UserDaoImpl implements UserDao {

    @Override
    public User getUserByNamePasswordAndRole(String name, String password, Class<? extends User> roleClass) {
        return null;
    }

    @Override
    public User getById(Long id, Connection connection) {
        throw new UnsupportedOperationException("Use getById(Long id");
    }

    @Override
    public User getById(Long id) {
        throw new UnsupportedOperationException("public User getById(Long id) Has no realisation yet");
    }
}
