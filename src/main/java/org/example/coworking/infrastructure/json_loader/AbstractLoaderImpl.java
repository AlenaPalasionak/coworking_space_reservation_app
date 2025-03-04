package org.example.coworking.infrastructure.json_loader;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.Logger;
import org.example.coworking.infrastructure.logger.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractLoaderImpl implements Loader {
    private static final Logger logger = Log.getLogger(AbstractLoaderImpl.class);

    protected String filePath;
    protected ObjectMapper objectMapper;

    public AbstractLoaderImpl(String filePath) {
        objectMapper = new ObjectMapper();
        this.filePath = filePath;
        objectMapper.registerModule(new JavaTimeModule());
    }

    public <T> List<T> load(Class<T> beanType) throws FileNotFoundException {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, beanType);
        List<T> list = new ArrayList<>();
        File jsonFile = new File(filePath);
        if (!jsonFile.exists()) {
            String message = "File with the name: " + filePath + " is not found";
            logger.error(message);
            throw new FileNotFoundException(message);
        }
        if (jsonFile.length() == 0) {
            logger.warn("JSON file " + jsonFile + " with the path " + filePath + " is empty");
        } else {
            try {
                list = objectMapper.readValue(jsonFile, javaType);
            } catch (IOException e) {
                logger.error("Can not read JSON into POJO: " + beanType.getName() + "\n" + e.getStackTrace());
                throw new RuntimeException(e.getMessage());
            }
        }
        return list != null ? list : Collections.emptyList();
    }

    public <T> void save(List<T> objects) {
        if (objects.isEmpty()) {
            logger.warn("List " + objects + "is empty");
        } else {

            try {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), objects);
            } catch (IOException e) {
                logger.error(" Unable to write data to the file. " + filePath + "\n" + e.getStackTrace());
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
