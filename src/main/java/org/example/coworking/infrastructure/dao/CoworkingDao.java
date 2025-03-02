package org.example.coworking.infrastructure.dao;

import org.example.coworking.model.CoworkingSpace;

import java.util.List;

public interface CoworkingDao {
    void add(CoworkingSpace coworkingSpace);
    void deleteById(int id);
    List<CoworkingSpace> getAllSpaces();
    void updateSpace(CoworkingSpace newCoworkingSpace);
    CoworkingSpace getById(int id);
    void getCoworkingPlacesFromJson();
    void saveToJSON();
}
