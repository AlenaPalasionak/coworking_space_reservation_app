package org.example.coworking.dao;

import org.example.coworking.dao.exception.DaoErrorCode;
import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.loader.Loader;
import org.example.coworking.model.User;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.example.coworking.logger.Log.TECHNICAL_LOGGER;

public class FileUserDao implements UserDao {
    private final Loader<User> userLoader;
    private static List<User> userCache;

    public FileUserDao(Loader<User> userLoader) {
        this.userLoader = userLoader;
        loadFromJson();
    }

    @Override
    public User getUserByNamePasswordAndRole(String name, String password, Class<? extends User> roleClass) throws EntityNotFoundException {
        Optional<User> possibleUser = userCache.stream()
                .filter(user -> user.getName().equals(name) &&
                        user.getPassword().equals(password) &&
                        user.getClass().equals(roleClass))
                .findFirst();
        if (possibleUser.isEmpty()) {
            throw new EntityNotFoundException("Failure to find user with the name: " + name, DaoErrorCode.USER_IS_NOT_FOUND);
        } else {
            return possibleUser.get();
        }
    }

    private void loadFromJson() {
        if (userCache == null) {
            try {
                userCache = userLoader.load(User.class);
            } catch (FileNotFoundException e) {
                TECHNICAL_LOGGER.error("Failure to load User List" + e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
