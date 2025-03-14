package org.example.coworking.infrastructure.dao;

import java.util.List;

public interface ModelDao<T, E extends Exception> {
    void add(T object);
    void delete(T object) throws E;
    T getById(int id) throws E;
    List<T> getAll();
    void load();
    void save();
}
