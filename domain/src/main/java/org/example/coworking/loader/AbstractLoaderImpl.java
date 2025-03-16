package org.example.coworking.loader;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public abstract class AbstractLoaderImpl<T> implements Loader<T> {
    protected static final Logger USER_OUTPUT_LOGGER = LoggerFactory.getLogger("UserOutputLogger");
    protected static final Logger TECHNICAL_LOGGER = LoggerFactory.getLogger("TechnicalLogger");

    protected final ObjectMapper objectMapper;

    public AbstractLoaderImpl() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    protected abstract String getFilepath();

    /**
     * Loads JSON-file and deserializes it into a list of objects.
     */
    public List<T> load(Class<T> beanType) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, beanType);
        List<T> list;

        try (InputStream inputStream = loadResourceAsStream()) {
            list = objectMapper.readValue(inputStream, javaType);
        } catch (IOException e) {
            String errorMessage = "Failed to parse JSON file: " + beanType.getName();
            USER_OUTPUT_LOGGER.error(errorMessage, e);
            TECHNICAL_LOGGER.error(errorMessage, e);
            throw new RuntimeException("Error reading JSON", e);
        }

        return list;
    }

    /**
     * Loads resources as InputStream.
     *
     * @return InputStream for JSON-file
     * @throws RuntimeException file is not found
     */

    public void save(List<T> objects) {
        File jsonFile = new File(getFilepath());

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, objects);
        } catch (IOException e) {
            String errorMessage = "Failed to write JSON file: " + getFilepath();
            USER_OUTPUT_LOGGER.error(errorMessage, e);
            TECHNICAL_LOGGER.error(errorMessage, e);
            throw new RuntimeException("Error writing JSON", e);
        }
    }

    private InputStream loadResourceAsStream() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(getFilepath());
        if (inputStream == null) {
            String message = "File not found in classpath: " + getFilepath();
            USER_OUTPUT_LOGGER.error(message);
            TECHNICAL_LOGGER.error(message);
            throw new RuntimeException(message);
        }
        return inputStream;
    }
}