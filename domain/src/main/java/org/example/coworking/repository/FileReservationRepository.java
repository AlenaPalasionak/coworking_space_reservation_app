package org.example.coworking.repository;

import jakarta.annotation.PreDestroy;
import org.example.coworking.repository.exception.DaoErrorCode;
import org.example.coworking.repository.exception.EntityNotFoundException;
import org.example.coworking.loader.Loader;
import org.example.coworking.entity.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.example.coworking.logger.Log.TECHNICAL_LOGGER;

@Repository("fileReservationRepository")
public class FileReservationRepository implements ReservationRepository {
    private static List<Reservation> reservationsCache;
    private final Loader<Reservation> reservationLoader;

    @Autowired
    public FileReservationRepository(Loader<Reservation> reservationLoader) {
        this.reservationLoader = reservationLoader;
        loadFromJson();
    }

    @Override
    public void create(Reservation reservation) {
        boolean isUniqueIdGenerated;
        Long generatedId;
        do {
            generatedId = IdGenerator.generateReservationId();
            Long finalGeneratedId = generatedId;
            isUniqueIdGenerated = reservationsCache.stream()
                    .anyMatch(r -> r.getId().equals(finalGeneratedId));
        } while (isUniqueIdGenerated);

        reservation.setId(generatedId);
        reservationsCache.add(reservation);
    }

    @Override
    public void delete(Reservation reservation) {
        reservationsCache.removeIf(r -> r.getId().equals(reservation.getId()));
    }

    @Override
    public Reservation getById(Long reservationId) throws EntityNotFoundException {
        return reservationsCache.stream()
                .filter(r -> r.getId().equals(reservationId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(String.format("Failure to get Reservation with id: %d",
                        reservationId), DaoErrorCode.RESERVATION_IS_NOT_FOUND));
    }

    @Override
    public List<Reservation> getAll() {
        return reservationsCache;
    }

    @Override
    public Set<Reservation> getAllReservationsByCoworking(Long coworkingId) {
        return reservationsCache.stream()
                .filter(reservation -> reservation.getCoworkingSpace().getId().equals(coworkingId))
                .collect(Collectors.toSet());
    }

    @Override
    public List<Reservation> getAllReservationsByCustomer(Long customerId) {
        return reservationsCache.stream()
                .filter(reservation -> reservation.getCustomer().getId().equals(customerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> getAllReservationsByAdmin(Long adminId) {
        List<Reservation> reservations = new ArrayList<>();
        if (reservationsCache.isEmpty()) {
            return reservations;
        } else {
            return reservationsCache.stream()
                    .filter(reservation -> reservation.getCoworkingSpace().getAdmin().getId().equals(adminId))
                    .collect(Collectors.toList());
        }
    }

    /**
     * Saves the current state of reservations cache to the json file.
     * This method should be called during application shutdown to persist
     * any in-memory reservations to disk. The data will be available for
     * loading when the application restarts.
     */
    @PreDestroy
    public void shutdown() {
        reservationLoader.save(reservationsCache);
    }

    /**
     * Loads reservations from JSON storage into the cache if not already loaded.
     * Subsequent calls will use the cached data.
     *
     * @throws RuntimeException if the reservation data file cannot be found or loaded,
     *                          wrapping the original FileNotFoundException. The exception will be logged
     *                          with technical details before being rethrown.
     */
    private void loadFromJson() {
        if (reservationsCache == null) {
            try {
                reservationsCache = reservationLoader.load(Reservation.class);
            } catch (FileNotFoundException e) {
                TECHNICAL_LOGGER.error("Failure to load Reservation List", e);
                throw new RuntimeException("Failure to load Reservation List", e);
            }
        }
    }
}
