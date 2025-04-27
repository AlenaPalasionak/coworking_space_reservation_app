package org.example.coworking.service;

import org.example.coworking.dao.UserDao;
import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(@Qualifier("jpaUserDao") UserDao userDao) {
        this.userDao = userDao;
    }

    public <T extends User> T getUserByNamePasswordAndAndRole(String name, String password, Class<T> role) throws EntityNotFoundException {
        return userDao.getUserByNamePasswordAndRole(name, password, role);
    }
}
