package org.example.coworking.infrastructure.dao;

import org.apache.logging.log4j.Logger;
import org.example.coworking.infrastructure.dao.exception.CoworkingNotFoundException;
import org.example.coworking.infrastructure.loader.Loader;
import org.example.coworking.infrastructure.logger.Log;
import org.example.coworking.model.CoworkingSpace;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

public class CoworkingDaoImpl implements CoworkingDao {
    private static final Logger logger = Log.getLogger(CoworkingDaoImpl.class);
    private static List<CoworkingSpace> coworkingSpacesCache;
    private final Loader<CoworkingSpace> coworkingSpaceLoader;

    public CoworkingDaoImpl(Loader<CoworkingSpace> coworkingSpaceLoader) {
        this.coworkingSpaceLoader = coworkingSpaceLoader;
    }

    @Override
    public void add(CoworkingSpace coworkingSpace) {
        boolean isUniqueIdGenerated;
        int generatedId;
        do {
            generatedId = IdGenerator.generateCoworkingId();
            isUniqueIdGenerated = true;

            for (CoworkingSpace coworkingSpaceFromCache : coworkingSpacesCache) {
                if (coworkingSpaceFromCache.getId() == generatedId) {
                    isUniqueIdGenerated = false;
                    break;
                }
            }
        } while (!isUniqueIdGenerated);
        coworkingSpace.setId(generatedId);
        coworkingSpacesCache.add(coworkingSpace);
    }

    @Override
    public void delete(CoworkingSpace coworkingSpace) throws CoworkingNotFoundException {
        int coworkingId = coworkingSpace.getId();
        if (checkIfNotExist(coworkingId)) {
            throw new CoworkingNotFoundException(coworkingId);
        }
        coworkingSpacesCache.removeIf(coworking -> coworking.getId() == coworkingId);
    }

    @Override
    public Optional<CoworkingSpace> getById(int coworkingId) throws CoworkingNotFoundException {
        Optional<CoworkingSpace> possibleCoworkingSpace;
        if (checkIfNotExist(coworkingId)) {
            throw new CoworkingNotFoundException(coworkingId);
        } else {
            possibleCoworkingSpace = coworkingSpacesCache.stream()
                    .filter(c -> c.getId() == coworkingId)
                    .findFirst();
        }
        return possibleCoworkingSpace;
    }

    @Override
    public List<CoworkingSpace> getAll() {
        return coworkingSpacesCache;
    }

    @Override
    public void load() {
        if (coworkingSpacesCache == null) {
            coworkingSpacesCache = getFromStorage();
        }
    }

    @Override
    public void save() {
        coworkingSpaceLoader.save(coworkingSpacesCache);
    }

    private boolean checkIfNotExist(int id) {
        return coworkingSpacesCache.stream().noneMatch(c -> c.getId() == id);
    }

    private List<CoworkingSpace> getFromStorage() {
        try {
            coworkingSpacesCache = coworkingSpaceLoader.load(CoworkingSpace.class);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return coworkingSpacesCache;
    }
}
