package org.example.coworking.service;

import org.example.coworking.infrastructure.dao.CoworkingDao;
import org.example.coworking.infrastructure.dao.CoworkingDaoImpl;
import org.example.coworking.infrastructure.logger.Log;
import org.example.coworking.model.*;
import org.example.coworking.service.exception.CoworkingNotFoundException;
import org.example.coworking.service.exception.ForbiddenActionException;

import java.util.List;

public class CoworkingServiceImpl implements CoworkingService {
    CoworkingDao coworkingDao;

    public CoworkingServiceImpl() {
        this.coworkingDao = new CoworkingDaoImpl();
    }

    @Override
    public void add(double price, CoworkingType coworkingType, Facility facility) {
        coworkingDao.add(new CoworkingSpace(price, coworkingType, facility));
    }

    @Override
    public void delete(User user, int coworkingId) throws ForbiddenActionException, CoworkingNotFoundException {
        if (user instanceof Customer) {
            Log.error("** CoworkingServiceImpl** " + "user.getClass() + \"s are not allowed to delete coworking spaces.");
            throw new ForbiddenActionException(user.getClass().getSimpleName() + "s are not allowed to delete coworking spaces.");
        }

        if (coworkingDao.getAllSpaces().stream().noneMatch(c -> c.getId() == coworkingId)) {
            throw new CoworkingNotFoundException("Coworking with id " + user.getId() + " not found");
        }

        coworkingDao.deleteById(coworkingId);
    }
    public List<CoworkingSpace> getAllSpaces()  {
        return coworkingDao.getAllSpaces();
    }

    @Override
    public CoworkingSpace getById(int id) throws CoworkingNotFoundException {
        if (coworkingDao.getAllSpaces().stream().noneMatch(c -> c.getId() == id)) {
            throw new CoworkingNotFoundException("Coworking with id " + id + " not found");
        }
        return coworkingDao.getById(id);
    }
}
