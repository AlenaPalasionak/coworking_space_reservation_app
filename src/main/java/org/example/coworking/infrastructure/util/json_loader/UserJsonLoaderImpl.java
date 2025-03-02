package org.example.coworking.infrastructure.util.json_loader;

import org.example.coworking.infrastructure.config.Config;

public class UserJsonLoaderImpl extends AbstractJsonLoader {
    public UserJsonLoaderImpl() {
        super(Config.getProperties().getProperty("users"));
    }
}