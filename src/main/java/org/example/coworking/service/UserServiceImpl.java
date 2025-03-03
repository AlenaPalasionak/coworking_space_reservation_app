package org.example.coworking.service;

import org.example.coworking.infrastructure.dao.UserDao;
import org.example.coworking.infrastructure.dao.UserDaoImpl;
import org.example.coworking.model.User;


import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl() {
        this.userDao = new UserDaoImpl();
    }

    @Override
    public List<User> getUsersFromJson() {
        return userDao.getUsersFromJson();
    }


}
