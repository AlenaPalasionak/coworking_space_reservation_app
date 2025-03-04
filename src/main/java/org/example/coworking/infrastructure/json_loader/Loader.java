package org.example.coworking.infrastructure.json_loader;

import java.io.FileNotFoundException;
import java.util.List;

public interface Loader {

    <T> List<T> load(Class<T> beanType) throws FileNotFoundException;
    <T> void save(List<T> data);
}
