package org.example.coworking.service;

import org.example.coworking.repository.UserRepository;
import org.example.coworking.repository.exception.EntityNotFoundException;
import org.example.coworking.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(@Qualifier("jpaUserRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public <T extends User> T getUserByNamePasswordAndAndRole(String name, String password, Class<T> role) throws EntityNotFoundException {
        return userRepository.getUserByNamePasswordAndRole(name, password, role);
    }
}
