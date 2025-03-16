package org.example.coworking.dao;

import org.example.coworking.model.User;

import java.util.List;

public interface UserDao {

    List<User> load();

}
