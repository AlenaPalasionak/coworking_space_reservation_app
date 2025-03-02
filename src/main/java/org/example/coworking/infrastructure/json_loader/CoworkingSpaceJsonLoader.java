package org.example.coworking.infrastructure.json_loader;

import org.example.coworking.infrastructure.config.Config;

public class CoworkingSpaceJsonLoader extends AbstractJsonLoaderImpl {

    public CoworkingSpaceJsonLoader() {
        super(Config.getProperties().getProperty("coworking_places"));
    }
}
