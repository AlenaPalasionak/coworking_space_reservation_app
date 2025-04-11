package org.example.coworking.service;

import org.example.coworking.dao.CoworkingDao;
import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.model.*;
import org.example.coworking.service.exception.ForbiddenActionException;
import org.example.coworking.service.exception.ServiceErrorCode;

import java.util.List;
import java.util.Set;

public class CoworkingServiceImpl implements CoworkingService {
    private final CoworkingDao coworkingDao;

    public CoworkingServiceImpl(CoworkingDao coworkingDao) {
        this.coworkingDao = coworkingDao;
    }

    @Override
    public void add(User admin, double price, CoworkingType coworkingType, Set<Facility> facilities) {
        coworkingDao.create(new CoworkingSpace(admin, price, coworkingType, facilities));
    }

    @Override
    public void delete(User admin, Long coworkingSpaceId) throws ForbiddenActionException, EntityNotFoundException {
        CoworkingSpace coworkingSpace = getById(coworkingSpaceId);
        if (coworkingSpace.getAdmin().getId().equals(admin.getId())) {
            coworkingDao.delete(coworkingSpace);
        } else {
            throw new ForbiddenActionException("Action is forbidden for the user: " + admin.getName(),
                    ServiceErrorCode.FORBIDDEN_ACTION);
        }
    }

    @Override
    public CoworkingSpace getById(Long coworkingId) throws EntityNotFoundException {
        return coworkingDao.getById(coworkingId);
    }

    @Override
    public List<CoworkingSpace> getAll() {
        return coworkingDao.getAll();

    }

    @Override
    public List<CoworkingSpace> getAllByAdmin(User admin) {
        return coworkingDao.getAllCoworkingSpacesByAdmin(admin.getId());
    }


}
