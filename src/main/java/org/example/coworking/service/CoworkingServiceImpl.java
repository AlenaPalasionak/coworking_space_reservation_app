package org.example.coworking.service;

import org.example.coworking.infrastructure.dao.CoworkingDao;
import org.example.coworking.infrastructure.dao.exception.CoworkingNotFoundException;
import org.example.coworking.model.*;
import org.example.coworking.service.exception.ForbiddenActionException;

import java.util.List;
import java.util.Optional;

public class CoworkingServiceImpl implements CoworkingService {
    CoworkingDao coworkingDao;

    public CoworkingServiceImpl(CoworkingDao coworkingDao) {
        this.coworkingDao = coworkingDao;
    }

    @Override
    public void add(double price, CoworkingType coworkingType, List<Facility> facilities) {
        coworkingDao.add(new CoworkingSpace(price, coworkingType, facilities));
    }

    @Override
    public void delete(User user, int coworkingId) throws ForbiddenActionException, CoworkingNotFoundException {
        if (user.getClass() == Customer.class) {
            throw new ForbiddenActionException(user.getClass());
        } else coworkingDao.deleteById(coworkingId);
    }

    @Override
    public List<CoworkingSpace> getAllSpaces() {
        return coworkingDao.getAllSpaces();
    }

    @Override
    public Optional<CoworkingSpace> getCoworkingByCoworkingId(int coworkingId) throws CoworkingNotFoundException {
        return coworkingDao.getById(coworkingId);
    }

    @Override
    public void getCoworkingPlacesFromJson() {
        coworkingDao.getCoworkingPlacesFromJson();
    }

    @Override
    public void saveToJSON() {
        coworkingDao.saveToJSON();
    }
}
