package org.example.coworking.infrastructure.dao;

import org.example.coworking.infrastructure.dao.exception.CoworkingNotFoundException;
import org.example.coworking.model.CoworkingSpace;

import java.util.List;
import java.util.Optional;

public interface CoworkingDao {
    void add(CoworkingSpace coworkingSpace);
    void deleteById(int id) throws CoworkingNotFoundException;
    List<CoworkingSpace> getAllSpaces();
    Optional<CoworkingSpace> getById(int id) throws CoworkingNotFoundException;
    void getCoworkingPlacesFromJson();
    void saveToJSON();
}
