package org.example.coworking.infrastructure.json_loader;

import org.example.coworking.infrastructure.config.PropertyConfig;

public class UserLoader extends AbstractLoaderImpl {
    public UserLoader() {
        super(PropertyConfig.getProperties().getProperty("users"));
    }
}