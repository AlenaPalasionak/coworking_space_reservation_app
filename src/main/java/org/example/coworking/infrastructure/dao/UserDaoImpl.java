package org.example.coworking.infrastructure.dao;

import org.example.coworking.infrastructure.loader.Loader;
import org.example.coworking.model.User;

import java.io.FileNotFoundException;
import java.util.List;

import static org.example.coworking.infrastructure.logger.Log.CONSOLE_LOGGER;
import static org.example.coworking.infrastructure.logger.Log.FILE_LOGGER;

public class UserDaoImpl implements UserDao {
    private final Loader<User> userLoader;
    private List<User> usersCache;

    public UserDaoImpl(Loader<User> userLoader) {
        this.userLoader = userLoader;
    }

    public List<User> load() {
        if (usersCache == null) {
            loadFromStorage();
        }
        return usersCache;
    }

    private void loadFromStorage() {
        try {
            usersCache = userLoader.load(User.class);
        } catch (FileNotFoundException e) {
            CONSOLE_LOGGER.error(e.getMessage());
            FILE_LOGGER.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
