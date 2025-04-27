package org.example.coworking.repository;

import jakarta.annotation.PreDestroy;
import org.example.coworking.repository.exception.DaoErrorCode;
import org.example.coworking.repository.exception.EntityNotFoundException;
import org.example.coworking.loader.Loader;
import org.example.coworking.entity.CoworkingSpace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.coworking.logger.Log.TECHNICAL_LOGGER;

@Repository("fileCoworkingRepository")
public class FileCoworkingRepository implements CoworkingRepository {
    private static List<CoworkingSpace> coworkingSpacesCache;
    private final Loader<CoworkingSpace> coworkingSpaceLoader;

    private final ReservationRepository reservationDao;

    @Autowired
    public FileCoworkingRepository(Loader<CoworkingSpace> coworkingSpaceLoader, @Qualifier("fileReservationRepository") ReservationRepository reservationDao) {
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
    public void delete(CoworkingSpace coworkingSpace) {
        coworkingSpacesCache.removeIf(c -> c.getId().equals(coworkingSpace.getId()));
        reservationDao.getAll().removeIf(reservation -> reservation.getCoworkingSpace().getId().equals(coworkingSpace.getId()));
    }

    @Override
    public CoworkingSpace getById(Long coworkingId) throws EntityNotFoundException {
        return coworkingSpacesCache.stream()
                .filter(c -> c.getId().equals(coworkingId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(String.format("Failure to find Coworking with id: %d", coworkingId),
                        DaoErrorCode.COWORKING_IS_NOT_FOUND));
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

    /**
     * Saves the current state of coworking spaces cache to the json file.
     * This method should be called during application shutdown to persist
     * any in-memory reservations to disk. The data will be available for
     * loading when the application restarts.
     */
    @PreDestroy
    public void shutdown() {
        coworkingSpaceLoader.save(coworkingSpacesCache);
    }

    /**
     * Loads coworking Spaces from JSON storage into the cache if not already loaded.
     * Subsequent calls will use the cached data.
     *
     * @throws RuntimeException if the coworking Spaces data file cannot be found or loaded,
     *                          wrapping the original FileNotFoundException. The exception will be logged
     *                          with technical details before being rethrown.
     */
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
