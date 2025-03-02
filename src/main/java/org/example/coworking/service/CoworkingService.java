package org.example.coworking.service;

import org.example.coworking.model.*;
import org.example.coworking.service.exception.CoworkingNotFoundException;
import org.example.coworking.service.exception.ForbiddenActionException;

import java.util.List;

public interface CoworkingService {
    void add(double price, CoworkingType coworkingType, Facility facility);
    void delete(User user, int coworkingId) throws ForbiddenActionException, CoworkingNotFoundException;
    List<CoworkingSpace> getAllSpaces();
    CoworkingSpace getById(int id) throws CoworkingNotFoundException;
}
