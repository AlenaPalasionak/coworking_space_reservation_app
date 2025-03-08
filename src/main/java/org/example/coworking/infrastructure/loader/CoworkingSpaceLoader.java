package org.example.coworking.infrastructure.loader;

import org.example.coworking.model.CoworkingSpace;

public class CoworkingSpaceLoader extends AbstractLoaderImpl<CoworkingSpace> {
    private final String filePath;

    public CoworkingSpaceLoader(String filePath) {
        super();
        this.filePath = filePath;
    }

    @Override
    protected String getFilepath() {
        return filePath;
    }
}


