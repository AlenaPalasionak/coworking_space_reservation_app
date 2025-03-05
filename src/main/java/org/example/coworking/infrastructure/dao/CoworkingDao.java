package org.example.coworking.infrastructure.dao;

import org.example.coworking.infrastructure.dao.exception.CoworkingNotFoundException;
import org.example.coworking.model.CoworkingSpace;

import java.util.List;
import java.util.Optional;

public interface CoworkingDao extends ModelDao<CoworkingSpace, CoworkingNotFoundException> {
    void add(CoworkingSpace coworkingSpace);
    void delete(CoworkingSpace coworking) throws CoworkingNotFoundException;
    Optional<CoworkingSpace> getById(int id) throws CoworkingNotFoundException;
    List<CoworkingSpace> getAll();
    void load();
    void save();
}
