package org.example.coworking.service;

import org.example.coworking.dao.exception.CoworkingNotFoundException;
import org.example.coworking.model.CoworkingSpace;
import org.example.coworking.model.CoworkingType;
import org.example.coworking.model.Facility;
import org.example.coworking.model.User;
import org.example.coworking.service.exception.ForbiddenActionException;

import java.util.List;

public interface CoworkingService {
    void load();
    void save();
    void add(User user, double price, CoworkingType coworkingType, List<Facility> facilities);
    void delete(User user, Long id) throws ForbiddenActionException, CoworkingNotFoundException;
    List<CoworkingSpace> getAllByUser(User user);
    CoworkingSpace getById(Long id) throws CoworkingNotFoundException;
}
