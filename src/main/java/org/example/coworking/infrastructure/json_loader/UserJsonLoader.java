package org.example.coworking.infrastructure.json_loader;

import org.example.coworking.infrastructure.config.Config;

public class UserJsonLoader extends AbstractJsonLoaderImpl {
    public UserJsonLoader() {
        super(Config.getProperties().getProperty("users"));
    }
}