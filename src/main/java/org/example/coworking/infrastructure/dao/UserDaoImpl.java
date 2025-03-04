package org.example.coworking.infrastructure.dao;

import org.example.coworking.infrastructure.json_loader.JsonLoader;
import org.example.coworking.model.User;

import java.util.List;

public class UserDaoImpl implements UserDao {

    private final JsonLoader userJsonLoader;
    private List<User> usersCache;

    public UserDaoImpl(JsonLoader userJsonLoader) {
        this.userJsonLoader = userJsonLoader;
    }

    public List<User> getUsersFromJson() {
        if (usersCache == null) {
            loadFromJson();
        }
        return usersCache;
    }

    private void loadFromJson() {
        usersCache = userJsonLoader.loadFromJson(User.class);
    }
}
