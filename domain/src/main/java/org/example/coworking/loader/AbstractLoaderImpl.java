package org.example.coworking.loader;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.example.coworking.logger.Log.USER_OUTPUT_LOGGER;
import static org.example.coworking.logger.Log.TECHNICAL_LOGGER;

/**
 * An abstract implementation of the {@link Loader} interface, providing common functionality
 * for loading and saving data of type {@link T} from and to a JSON file.
 * <p>
 * This class uses the {@link ObjectMapper} for JSON serialization and deserialization,
 * and it requires subclasses to define the file path where data will be loaded from or saved to.
 * </p>
 *
 * @param <T> the type of the data to be loaded and saved
 */
public abstract class AbstractLoaderImpl<T> implements Loader<T> {

    /**
     * An instance of {@link ObjectMapper} used for converting between Java objects and JSON.
     */
    protected ObjectMapper objectMapper;

    public AbstractLoaderImpl() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Abstract method to retrieve the file path where the data is stored.
     * <p>
     * This method must be implemented by subclasses to return the specific file path.
     * </p>
     *
     * @return the file path where data is stored
     */
    protected abstract String getFilepath();

    @Override
    public List<T> load(Class<T> beanType) throws FileNotFoundException {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, beanType);
        List<T> list = new ArrayList<>();
        File jsonFile = new File(getFilepath());
        if (!jsonFile.exists()) {
            String message = "File with the name: " + getFilepath() + " is not found";
            USER_OUTPUT_LOGGER.error(message);
            TECHNICAL_LOGGER.error(message);
            throw new FileNotFoundException(message);
        }
        if (jsonFile.length() == 0) {
            USER_OUTPUT_LOGGER.warn("JSON file " + jsonFile + " is empty");
            TECHNICAL_LOGGER.warn("JSON file " + jsonFile + " is empty");
        } else {
            try {
                list = objectMapper.readValue(jsonFile, javaType);
            } catch (IOException e) {
                USER_OUTPUT_LOGGER.error("Failed to parse JSON file:  " + beanType.getName() + "\n", e);
                TECHNICAL_LOGGER.error("Failed to parse JSON file:  " + beanType.getName() + "\n", e);
                throw new RuntimeException("Error reading JSON", e);
            }
        }
        return list;
    }

    @Override
    public void save(List<T> objects) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(getFilepath()), objects);
        } catch (IOException e) {
            USER_OUTPUT_LOGGER.error(" Failed to write JSON file: " + getFilepath(), e);
            TECHNICAL_LOGGER.error(" Failed to write JSON file: " + getFilepath(), e);
            throw new RuntimeException("Error writing JSON", e);
        }
    }
}