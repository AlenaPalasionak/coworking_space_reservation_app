package org.example.coworking.dao;

import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.model.CoworkingSpace;

import java.util.List;

public class JpaCoworkingDao implements CoworkingDao {
    @Override
    public List<CoworkingSpace> getAllCoworkingSpacesByAdmin(Long adminId) {
        return null;
    }

    @Override
    public void create(CoworkingSpace object) {

    }

    @Override
    public void delete(CoworkingSpace object) throws EntityNotFoundException {

    }

    @Override
    public CoworkingSpace getById(Long id) throws EntityNotFoundException {
        return null;
    }

    @Override
    public List<CoworkingSpace> getAll() {
        return null;
    }
}
