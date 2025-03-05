package org.example.coworking.infrastructure.dao;

import org.apache.logging.log4j.Logger;
import org.example.coworking.infrastructure.loader.Loader;
import org.example.coworking.infrastructure.logger.Log;
import org.example.coworking.model.User;

import java.io.FileNotFoundException;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private static final Logger logger = Log.getLogger(UserDaoImpl.class);

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
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
