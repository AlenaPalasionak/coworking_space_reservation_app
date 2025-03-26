package org.example.coworking.dao;

import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.model.User;

import java.sql.Connection;
import java.util.List;

public class UserDaoImpl implements UserDao {

    @Override
    public void add(User object) {

    }

    @Override
    public void delete(User object) throws EntityNotFoundException {

    }

    @Override
    public User getById(Long id) throws EntityNotFoundException {
        return null;
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public User getUserByNamePasswordAndRole(String name, String password, Class<? extends User> roleClass) {
        return null;
    }

    @Override
    public User getById(Long id, Connection connection) {
        throw new UnsupportedOperationException("Use getById(Long id");
    }
}
