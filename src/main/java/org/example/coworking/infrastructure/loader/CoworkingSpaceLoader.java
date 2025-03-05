package org.example.coworking.infrastructure.loader;

import org.example.coworking.infrastructure.config.PropertyConfig;

public class CoworkingSpaceLoader extends AbstractLoaderImpl {

    public CoworkingSpaceLoader() {
        super(PropertyConfig.getProperties().getProperty("coworking_places"));
    }
}
