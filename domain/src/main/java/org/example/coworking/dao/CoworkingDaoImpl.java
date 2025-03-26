package org.example.coworking.dao;

import org.example.coworking.dao.exception.DaoErrorCode;
import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.loader.Loader;
import org.example.coworking.model.CoworkingSpace;

import java.util.List;

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
    public void delete(CoworkingSpace coworkingSpace) throws EntityNotFoundException {
        Long coworkingId = coworkingSpace.getId();
        if (checkIfNotExist(coworkingId)) {
            throw new EntityNotFoundException("Coworking with id: " + coworkingId + " is not found"
                    , DaoErrorCode.COWORKING_IS_NOT_FOUND);
        }
        coworkingSpacesCache
                .removeIf(coworking -> coworking.getId().equals(coworkingId));
    }

    @Override
    public CoworkingSpace getById(Long coworkingId) throws EntityNotFoundException {
        return coworkingSpacesCache.stream()
                .filter(c -> c.getId().equals(coworkingId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Coworking with id: " + coworkingId + " is not found"
                        , DaoErrorCode.COWORKING_IS_NOT_FOUND));
    }

    @Override
    public List<CoworkingSpace> getAll() {
        return coworkingSpacesCache;
    }

    private boolean checkIfNotExist(Long id) {
        return coworkingSpacesCache.stream()
                .noneMatch(c -> c.getId().equals(id));
    }
}
