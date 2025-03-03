package org.example.coworking.infrastructure.json_loader;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.coworking.infrastructure.logger.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractJsonLoaderImpl implements JsonLoader {
    protected String filePath;
    protected ObjectMapper objectMapper;

    public AbstractJsonLoaderImpl(String filePath) {
        objectMapper = new ObjectMapper();
        this.filePath = filePath;
        objectMapper.registerModule(new JavaTimeModule());
    }

    public <T> List<T> loadFromJson(Class<T> beanType) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, beanType);
        List<T> list = new ArrayList<>();
        File jsonFile = new File(filePath);
        if (jsonFile.length() == 0) {
            Log.warning("JSON file " + jsonFile + " with the path " + filePath + "is empty");
        } else {
            try {
                list = objectMapper.readValue(jsonFile, javaType);
            } catch (IOException e) {
                Log.error("Can not read JSON into POJO: " + beanType.getName() + "\n" + e.getStackTrace());
                throw new RuntimeException(e.getMessage());
            }
        }
        return list != null ? list : Collections.emptyList();
    }

    public <T> void convertToJson(List<T> objects) {
        if (objects.isEmpty()) {
            Log.warning("List " + objects + "is empty");
        } else {

            try {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), objects);
            } catch (IOException e) {
                Log.error(" Unable to write data to the file. " + filePath + "\n" + e.getStackTrace());
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
