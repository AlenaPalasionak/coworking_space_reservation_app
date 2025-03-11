package org.example.coworking.service;

import org.example.coworking.infrastructure.dao.CoworkingDao;
import org.example.coworking.infrastructure.dao.exception.CoworkingNotFoundException;
import org.example.coworking.model.*;
import org.example.coworking.service.exception.ForbiddenActionException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CoworkingServiceImpl implements CoworkingService {
    private final CoworkingDao coworkingDao;

    public CoworkingServiceImpl(CoworkingDao coworkingDao) {
        this.coworkingDao = coworkingDao;
    }

    @Override
    public void add(CoworkingSpace coworkingSpace) {
        coworkingDao.add(coworkingSpace);
    }

    @Override
    public void delete(User user, int id) throws ForbiddenActionException, CoworkingNotFoundException {
        if (user.getClass() == Customer.class) {
            throw new ForbiddenActionException(user.getClass());
        }

        Optional<CoworkingSpace> possibleCoworking = getById(id);
        if (possibleCoworking.isPresent()) {
            CoworkingSpace coworkingSpace = possibleCoworking.get();
            coworkingDao.delete(coworkingSpace);
        } else {
            throw new CoworkingNotFoundException(id);
        }
    }

    @Override
    public List<CoworkingSpace> getAllByUser(User user) {
        if (user != null && user.getClass() == Admin.class) {
            return coworkingDao.getAll().stream()
                    .filter(coworking -> coworking.getAdmin().getId() == user.getId())
                    .collect(Collectors.toList());
        } else {
            return coworkingDao.getAll();
        }
    }

    @Override
    public Optional<CoworkingSpace> getById(int coworkingId) throws CoworkingNotFoundException {
        return coworkingDao.getById(coworkingId);
    }

    @Override
    public void load() {
        coworkingDao.load();
    }

    @Override
    public void save() {
        coworkingDao.save();
    }
}
