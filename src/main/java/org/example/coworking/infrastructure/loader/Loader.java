package org.example.coworking.infrastructure.loader;

import java.io.FileNotFoundException;
import java.util.List;

public interface Loader<T> {

    List<T> load(Class<T> beanType) throws FileNotFoundException;
    void save(List<T> data);
}
