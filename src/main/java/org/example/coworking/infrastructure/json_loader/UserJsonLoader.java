package org.example.coworking.infrastructure.json_loader;

import org.example.coworking.infrastructure.config.PropertyConfig;

public class UserJsonLoader extends AbstractJsonLoaderImpl {
    public UserJsonLoader() {
        super(PropertyConfig.getProperties().getProperty("users"));
    }
}