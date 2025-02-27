package org.example.coworking.infrastructure.dao;

import org.example.coworking.model.CoworkingSpace;

import java.util.ArrayList;
import java.util.List;

public class CoworkingDaoImpl implements CoworkingDao {
    private static final List<CoworkingSpace> coworkingSpacesCache = new ArrayList<>();

    @Override
    public void add(CoworkingSpace coworkingSpace) {
        coworkingSpace.setId(IdGenerator.generateCoworkingId());
        coworkingSpacesCache.add(coworkingSpace);
    }

    @Override
    public List<CoworkingSpace> getAllSpaces() {
        return coworkingSpacesCache;
    }

    @Override
    public void deleteById(int coworkingId) {
        coworkingSpacesCache.removeIf(coworking -> coworking.getId() == coworkingId);
    }

    @Override
    public void updateSpace(CoworkingSpace newCoworkingSpace) {
        for (int i = 0; i < coworkingSpacesCache.size(); i++) {
            CoworkingSpace oldCoworking = coworkingSpacesCache.get(i);
            if (oldCoworking.getId() == newCoworkingSpace.getId()) {
                coworkingSpacesCache.set(i, newCoworkingSpace);
                break;
            }
        }
    }

    @Override
    public CoworkingSpace getById(int id) {
        return coworkingSpacesCache.get(id);
    }
}
