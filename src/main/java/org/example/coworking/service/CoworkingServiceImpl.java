package org.example.coworking.service;

import org.example.coworking.dao.CoworkingDao;
import org.example.coworking.dao.CoworkingDaoImpl;
import org.example.coworking.model.Coworking;

import java.util.List;

public class CoworkingServiceImpl implements CoworkingServise {
    CoworkingDao coworkingDao = new CoworkingDaoImpl();

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
