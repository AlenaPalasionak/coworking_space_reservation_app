package org.example.coworking.infrastructure.util.json_loader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.coworking.infrastructure.logger.Log;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public abstract class AbstractJsonLoader implements JsonLoader {
    protected String filePath;
    protected ObjectMapper objectMapper;

    public AbstractJsonLoader(String filePath) {
        objectMapper = new ObjectMapper();
        this.filePath = filePath;
    }

    public <T> List<T> loadFromJson(Class<T> beanType) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, beanType);
        List<T> list;

        try {
            list = objectMapper.readValue(new File(filePath), javaType);
        } catch (JsonProcessingException e) {
            Log.error("*Can not transfer JSON into POJO" + e.getMessage());
            throw new RuntimeException(e);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return list != null ? list : Collections.emptyList();
    }

    public <T> void convertToJson(List<T> objects) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), objects);
        } catch (IOException e) {
            Log.warning("** AbstractJsonLoader ** unable to write data to the file. " + e.getMessage());
        }
    }

}
