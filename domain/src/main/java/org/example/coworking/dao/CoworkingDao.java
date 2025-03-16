package org.example.coworking.dao;

import org.example.coworking.dao.exception.CoworkingNotFoundException;
import org.example.coworking.model.CoworkingSpace;

import java.util.List;

public interface CoworkingDao extends ModelDao<CoworkingSpace, CoworkingNotFoundException> {
    void add(CoworkingSpace coworkingSpace);
    void delete(CoworkingSpace coworking) throws CoworkingNotFoundException;
    CoworkingSpace getById(Long id) throws CoworkingNotFoundException;
    List<CoworkingSpace> getAll();
    void load();
    void save();
}
