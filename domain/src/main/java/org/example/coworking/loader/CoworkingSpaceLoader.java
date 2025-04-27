package org.example.coworking.loader;

import org.example.coworking.entity.CoworkingSpace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class CoworkingSpaceLoader extends AbstractLoaderImpl<CoworkingSpace> {
    private final String filePath;

    @Autowired
    public CoworkingSpaceLoader(@Value("${coworking.places.path}") String filePath) {
        super();
        this.filePath = filePath;
    }

    @Override
    protected String getFilepath() {
        return filePath;
    }
}


