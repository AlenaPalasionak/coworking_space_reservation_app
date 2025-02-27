package org.example.coworking.infrastructure.dao;

import org.example.coworking.model.User;
import org.example.coworking.infrastructure.util.json_loader.JsonLoader;
import org.example.coworking.infrastructure.util.json_loader.UserJsonLoaderImpl;

import java.util.List;

public class UserDaoImpl implements UserDao {

    private final JsonLoader userJsonLoader;
    private List<User> usersCache;

    public UserDaoImpl() {
        this.userJsonLoader = new UserJsonLoaderImpl();
    }

    public List<User> getUsersFromJson() {
        if (usersCache == null) {
            loadFromJson();
        }
        return usersCache;
    }

    @Override
    public void convertToJson(List<User> users) {
        userJsonLoader.convertToJson(users);
    }

    private void loadFromJson(){
        usersCache = userJsonLoader.loadFromJson(User.class);
    }
}
