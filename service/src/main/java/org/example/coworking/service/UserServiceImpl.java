package org.example.coworking.service;

import org.example.coworking.dao.UserDao;
import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.model.User;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    public <T extends User> T getUserByNamePasswordAndAndRole(String name, String password, Class<T> role) throws EntityNotFoundException {
        return userDao.getUserByNamePasswordAndRole(name, password, role);
    }
}
