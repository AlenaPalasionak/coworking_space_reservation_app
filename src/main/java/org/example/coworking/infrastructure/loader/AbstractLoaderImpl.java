package org.example.coworking.infrastructure.loader;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.example.coworking.infrastructure.logger.Log.CONSOLE_LOGGER;
import static org.example.coworking.infrastructure.logger.Log.FILE_LOGGER;

public abstract class AbstractLoaderImpl<T> implements Loader<T> {
    protected ObjectMapper objectMapper;

    public AbstractLoaderImpl() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    protected abstract String getFilepath();

    public List<T> load(Class<T> beanType) throws FileNotFoundException {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, beanType);
        List<T> list = new ArrayList<>();
        File jsonFile = new File(getFilepath());
        if (!jsonFile.exists()) {
            String message = "File with the name: " + getFilepath() + " is not found";
            CONSOLE_LOGGER.error(message);
            FILE_LOGGER.error(message);
            throw new FileNotFoundException(message);
        }
        if (jsonFile.length() == 0) {
            CONSOLE_LOGGER.warn("JSON file " + jsonFile + " is empty");
            FILE_LOGGER.warn("JSON file " + jsonFile + " is empty");
        } else {
            try {
                list = objectMapper.readValue(jsonFile, javaType);
            } catch (IOException e) {
                CONSOLE_LOGGER.error("Failed to parse JSON file:  " + beanType.getName() + "\n", e);
                FILE_LOGGER.error("Failed to parse JSON file:  " + beanType.getName() + "\n", e);
                throw new RuntimeException("Error reading JSON", e);
            }
        }
        return list;
    }

    public void save(List<T> objects) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(getFilepath()), objects);
        } catch (IOException e) {
            CONSOLE_LOGGER.error(" Failed to write JSON file: " + getFilepath(), e);
            FILE_LOGGER.error(" Failed to write JSON file: " + getFilepath(), e);
            throw new RuntimeException("Error writing JSON", e);
        }
    }
}