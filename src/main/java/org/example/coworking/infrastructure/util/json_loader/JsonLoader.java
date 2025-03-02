package org.example.coworking.infrastructure.util.json_loader;

import java.util.List;

public interface JsonLoader {

    <T> List<T> loadFromJson(Class<T> beanType);
    <T> void convertToJson(List<T> data);
}
