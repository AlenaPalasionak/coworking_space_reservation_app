package org.example.coworking.service;

import org.example.coworking.repository.CoworkingRepository;
import org.example.coworking.repository.CoworkingRepositoryImpl;
import org.example.coworking.model.Coworking;

import java.util.List;

public class CoworkingServiceImpl implements CoworkingServise {
    CoworkingRepository coworkingDao = new CoworkingRepositoryImpl();

    @Override
    public void addSpace(Coworking space) {
        coworkingDao.addSpace(space);
    }

    @Override
    public List<Coworking> getAllSpaces() {
        return coworkingDao.getAllSpaces();
    }

    @Override
    public void removeSpace(int id) {
        coworkingDao.removeSpace(id);
    }

    @Override
    public void updateSpace(Coworking newCoworkingVersion, int oldCoworkingVersionId) {
        coworkingDao.updateSpace(newCoworkingVersion, oldCoworkingVersionId);
    }

    @Override
    public Coworking getById(int id) {
        return coworkingDao.getById(id);
    }
}
