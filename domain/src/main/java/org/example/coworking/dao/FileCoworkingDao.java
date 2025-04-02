package org.example.coworking.dao;

import org.example.coworking.dao.exception.DaoErrorCode;
import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.loader.Loader;
import org.example.coworking.model.CoworkingSpace;

import java.io.FileNotFoundException;
import java.util.List;
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
        reservationDao.getAll().removeIf(reservation -> reservation.getCoworkingSpace().getId().equals(coworkingSpace.getId()));
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


    @Override
    public List<CoworkingSpace> getAllCoworkingSpacesByAdmin(Long adminId) {
        return coworkingSpacesCache.stream().filter(coworkingSpace -> coworkingSpace.getAdmin().getId().equals(adminId))
                .collect(Collectors.toList());
    }

    private boolean checkIfNotExist(Long id) {
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
                TECHNICAL_LOGGER.error("Failure to load Coworking Space List" + e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
