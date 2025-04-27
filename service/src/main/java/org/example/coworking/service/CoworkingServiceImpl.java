package org.example.coworking.service;

import org.example.coworking.dao.CoworkingDao;
import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.model.Admin;
import org.example.coworking.model.CoworkingSpace;
import org.example.coworking.model.CoworkingType;
import org.example.coworking.model.Facility;
import org.example.coworking.service.exception.ForbiddenActionException;
import org.example.coworking.service.exception.ServiceErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CoworkingServiceImpl implements CoworkingService {
    private final CoworkingDao coworkingDao;

    @Autowired
    public CoworkingServiceImpl(@Qualifier("jpaCoworkingDao") CoworkingDao coworkingDao) {
        this.coworkingDao = coworkingDao;
    }

    @Override
    public void add(Admin admin, double price, CoworkingType coworkingType, Set<Facility> facilities) {
        coworkingDao.create(new CoworkingSpace(admin, price, coworkingType, facilities));
    }

    @Override
    public void delete(Admin admin, Long coworkingSpaceId) throws ForbiddenActionException, EntityNotFoundException {
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
    public List<CoworkingSpace> getAllByAdmin(Admin admin) {
        return coworkingDao.getAllCoworkingSpacesByAdmin(admin.getId());
    }


}
