package org.example.coworking.infrastructure.loader;

import org.example.coworking.infrastructure.config.PropertyConfig;
import org.example.coworking.model.User;

public class UserLoader extends AbstractLoaderImpl<User> {
    public UserLoader() {
        super(PropertyConfig.getProperties().getProperty("users"));
    }
}