package org.example.coworking.infrastructure.dao;

import org.example.coworking.model.User;

import java.util.List;

public interface UserDao {

     List<User> getUsersFromJson();
     void convertToJson(List<User> users);
}
