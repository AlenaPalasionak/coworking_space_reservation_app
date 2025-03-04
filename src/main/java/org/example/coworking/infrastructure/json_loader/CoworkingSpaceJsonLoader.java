package org.example.coworking.infrastructure.json_loader;

import org.example.coworking.infrastructure.config.PropertyConfig;

public class CoworkingSpaceJsonLoader extends AbstractJsonLoaderImpl {

    public CoworkingSpaceJsonLoader() {
        super(PropertyConfig.getProperties().getProperty("coworking_places"));
    }
}
