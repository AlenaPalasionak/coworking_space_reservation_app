package org.example.coworking.infrastructure.dao;

import org.example.coworking.infrastructure.dao.exception.CoworkingNotFoundException;
import org.example.coworking.infrastructure.dao.exception.DaoErrorCode;
import org.example.coworking.infrastructure.loader.Loader;
import org.example.coworking.model.CoworkingSpace;

import java.io.FileNotFoundException;
import java.util.List;

import static org.example.coworking.infrastructure.logger.Log.TECHNICAL_LOGGER;

public class CoworkingDaoImpl implements CoworkingDao {
    private static List<CoworkingSpace> coworkingSpacesCache;
    private final Loader<CoworkingSpace> coworkingSpaceLoader;

    public CoworkingDaoImpl(Loader<CoworkingSpace> coworkingSpaceLoader) {
        this.coworkingSpaceLoader = coworkingSpaceLoader;
    }

    @Override
    public void add(CoworkingSpace coworkingSpace) {
        Long generatedId;
        boolean isUniqueIdGenerated;

        do {
            generatedId = IdGenerator.generateCoworkingId();
            Long finalGeneratedId = generatedId;
            isUniqueIdGenerated = coworkingSpacesCache.stream()
                    .anyMatch(c -> c.getId().equals(finalGeneratedId));
        } while (isUniqueIdGenerated);

        coworkingSpace.setId(generatedId);
        coworkingSpacesCache.add(coworkingSpace);
    }

    @Override
    public void delete(CoworkingSpace coworkingSpace) throws CoworkingNotFoundException {
        Long coworkingId = coworkingSpace.getId();
        if (checkIfNotExist(coworkingId)) {
            throw new CoworkingNotFoundException("Coworking with id: " + coworkingId + " is not found"
                    , DaoErrorCode.COWORKING_IS_NOT_FOUND);
        }
        coworkingSpacesCache
                .removeIf(coworking -> coworking.getId().equals(coworkingId));
    }

    @Override
    public CoworkingSpace getById(Long coworkingId) throws CoworkingNotFoundException {
        return coworkingSpacesCache.stream()
                .filter(c -> c.getId().equals(coworkingId))
                .findFirst()
                .orElseThrow(() -> new CoworkingNotFoundException("Coworking with id: " + coworkingId + " is not found"
                        , DaoErrorCode.COWORKING_IS_NOT_FOUND));
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

    private boolean checkIfNotExist(Long id) {
        return coworkingSpacesCache.stream()
                .noneMatch(c -> c.getId().equals(id));
    }

    private List<CoworkingSpace> getFromStorage() {
        try {
            coworkingSpacesCache = coworkingSpaceLoader.load(CoworkingSpace.class);
        } catch (FileNotFoundException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return coworkingSpacesCache;
    }
}
