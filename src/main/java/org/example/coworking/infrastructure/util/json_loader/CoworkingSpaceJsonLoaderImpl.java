package org.example.coworking.infrastructure.util.json_loader;

import org.example.coworking.infrastructure.config.Config;

public class CoworkingSpaceJsonLoaderImpl extends AbstractJsonLoader {

    public CoworkingSpaceJsonLoaderImpl() {
        super(Config.getProperties().getProperty("coworking_places"));
    }
}
