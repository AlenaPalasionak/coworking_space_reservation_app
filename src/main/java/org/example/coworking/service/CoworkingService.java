package org.example.coworking.service;

import org.example.coworking.infrastructure.dao.exception.CoworkingNotFoundException;
import org.example.coworking.model.CoworkingSpace;
import org.example.coworking.model.CoworkingType;
import org.example.coworking.model.Facility;
import org.example.coworking.model.User;
import org.example.coworking.service.exception.ForbiddenActionException;

import java.util.List;
import java.util.Optional;

public interface CoworkingService {
    void add(double price, CoworkingType coworkingType, List<Facility> facilities);
    void delete(User user, int coworkingId) throws ForbiddenActionException, CoworkingNotFoundException;
    List<CoworkingSpace> getAllSpaces();
    Optional<CoworkingSpace> getCoworkingByCoworkingId(int id) throws CoworkingNotFoundException;
    void getCoworkingPlacesFromJson();
    void saveToJSON();
}
