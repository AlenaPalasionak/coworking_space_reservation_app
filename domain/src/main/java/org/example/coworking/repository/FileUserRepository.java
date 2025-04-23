package org.example.coworking.repository;

import org.example.coworking.repository.exception.DaoErrorCode;
import org.example.coworking.repository.exception.EntityNotFoundException;
import org.example.coworking.loader.Loader;
import org.example.coworking.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.example.coworking.logger.Log.TECHNICAL_LOGGER;

@Repository("fileUserDao")
public class FileUserRepository implements UserRepository {
    private final Loader<User> userLoader;
    private static List<User> userCache;
    @Autowired
    public FileUserRepository(Loader<User> userLoader) {
        this.userLoader = userLoader;
        loadFromJson();
    }

    @Override
    public <T extends User> T getUserByNamePasswordAndRole(String name, String password, Class<T> role) throws EntityNotFoundException {
        Optional<User> possibleUser = userCache.stream()
                .filter(user -> user.getName().equals(name) &&
                        user.getPassword().equals(password) &&
                        user.getClass().equals(role))
                .findFirst();
        if (possibleUser.isEmpty()) {
            throw new EntityNotFoundException("Failure to find user with the name: " + name, DaoErrorCode.USER_IS_NOT_FOUND);
        } else {
            return role.cast(possibleUser.get());
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
