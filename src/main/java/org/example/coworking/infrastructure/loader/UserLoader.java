package org.example.coworking.infrastructure.loader;

import org.example.coworking.model.User;

public class UserLoader extends AbstractLoaderImpl<User> {

    public UserLoader(String filePath) {
        super(filePath);
    }
}