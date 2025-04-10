package org.example.coworking.dao;

import org.example.coworking.dao.exception.DaoErrorCode;
import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.loader.Loader;
import org.example.coworking.model.CoworkingSpace;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.example.coworking.logger.Log.TECHNICAL_LOGGER;

public class FileCoworkingDao implements CoworkingDao {
    private static List<CoworkingSpace> coworkingSpacesCache;
    private final Loader<CoworkingSpace> coworkingSpaceLoader;

    private final ReservationDao reservationDao;

    public FileCoworkingDao(Loader<CoworkingSpace> coworkingSpaceLoader, ReservationDao reservationDao) {
        this.coworkingSpaceLoader = coworkingSpaceLoader;
        this.reservationDao = reservationDao;
        loadFromJson();
    }

    @Override
    public void create(CoworkingSpace coworkingSpace) {
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
    public void delete(Long coworkingSpaceId) throws EntityNotFoundException {//
        if (checkIfNotExist(coworkingSpaceId)) {
            throw new EntityNotFoundException(String.format("Failure to delete Coworking with id: %d. Coworking is not found."
                    , coworkingSpaceId), DaoErrorCode.COWORKING_IS_NOT_FOUND);
        }
        coworkingSpacesCache
                .removeIf(coworking -> coworking.getId().equals(coworkingSpaceId));
        reservationDao.getAll().removeIf(reservation -> reservation.getCoworkingSpace().getId().equals(coworkingSpaceId));
    }

    @Override
    public CoworkingSpace getById(Long coworkingId) throws EntityNotFoundException {
        return coworkingSpacesCache.stream()
                .filter(c -> c.getId().equals(coworkingId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(String.format("Failure to find Coworking with id: %d", coworkingId)
                        , DaoErrorCode.COWORKING_IS_NOT_FOUND));
    }

    @Override
    public List<CoworkingSpace> getAll() {
        return coworkingSpacesCache;
    }


    @Override
    public List<CoworkingSpace> getAllCoworkingSpacesByAdmin(Long adminId) {
        return coworkingSpacesCache.stream().filter(coworkingSpace -> coworkingSpace.getAdmin().getId().equals(adminId))
                .collect(Collectors.toList());
    }

    @Override
    public Long getAdminIdByCoworkingSpaceId(Long coworkingSpaceId) throws EntityNotFoundException {
        Optional<CoworkingSpace> possibleCoworkingSpace = coworkingSpacesCache.stream()
                .filter(coworkingSpace -> coworkingSpace.getId().equals(coworkingSpaceId))
                .findFirst();
        if (possibleCoworkingSpace.isEmpty()) {
            throw new EntityNotFoundException(String.format("Failure to find Coworking with id: %d"
                    , coworkingSpaceId), DaoErrorCode.COWORKING_IS_NOT_FOUND);
        } else {
            return possibleCoworkingSpace.get().getAdmin().getId();
        }
    }

    private boolean checkIfNotExist(Long id) {//service method
        return coworkingSpacesCache.stream()
                .noneMatch(c -> c.getId().equals(id));
    }

    public void shutdown() {
        coworkingSpaceLoader.save(coworkingSpacesCache);
    }

    private void loadFromJson() {
        if (coworkingSpacesCache == null) {
            try {
                coworkingSpacesCache = coworkingSpaceLoader.load(CoworkingSpace.class);
            } catch (FileNotFoundException e) {
                TECHNICAL_LOGGER.error("Failure to load Coworking Space List", e);
                throw new RuntimeException("Failure to load Coworking Space List", e);
            }
        }
    }
}
