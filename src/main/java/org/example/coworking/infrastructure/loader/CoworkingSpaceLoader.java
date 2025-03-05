package org.example.coworking.infrastructure.loader;

import org.example.coworking.infrastructure.config.PropertyConfig;
import org.example.coworking.model.CoworkingSpace;

public class CoworkingSpaceLoader extends AbstractLoaderImpl<CoworkingSpace> {

    public CoworkingSpaceLoader() {
        super(PropertyConfig.getProperties().getProperty("coworking_places"));
    }
}
