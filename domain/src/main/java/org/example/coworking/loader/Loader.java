package org.example.coworking.loader;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * This interface defines the operations for loading and saving data of a specific type.
 * It provides methods to load data from a source into a list of objects and save the data to a storage.
 *
 * @param <T> the type of the data to be loaded and saved
 */
public interface Loader<T> {

    /**
     * Loads data of the specified type from a storage.
     *
     * @param beanType the class type of the data to be loaded
     * @return a list of objects of type {@link T} loaded from the storage
     * @throws FileNotFoundException if the file or data storage is not found
     */
    List<T> load(Class<T> beanType) throws FileNotFoundException;

    /**
     * Saves the provided data to a storage.
     *
     * @param data the list of {@link T} objects to be saved
     */
    void save(List<T> data);
}
