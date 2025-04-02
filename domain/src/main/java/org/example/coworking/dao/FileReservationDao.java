package org.example.coworking.dao;

import org.example.coworking.dao.exception.DaoErrorCode;
import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.loader.Loader;
import org.example.coworking.model.Reservation;
import org.example.coworking.model.ReservationPeriod;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.example.coworking.logger.Log.TECHNICAL_LOGGER;

public class FileReservationDao implements ReservationDao {
    private static List<Reservation> reservationsCache;
    private final Loader<Reservation> reservationLoader;

    public FileReservationDao(Loader<Reservation> reservationLoader) {
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
    public void delete(Reservation reservation) throws EntityNotFoundException {
        if (checkIfNotExist(reservation.getId())) {
            throw new EntityNotFoundException(String.format("Failure to delete Reservation with id: %d. Reservation is not found."
                    , reservation.getId()), DaoErrorCode.RESERVATION_IS_NOT_FOUND);
        }
        reservationsCache.remove(reservation);
    }

    @Override
    public Reservation getById(Long reservationId) throws EntityNotFoundException {
        return reservationsCache.stream()
                .filter(r -> r.getId().equals(reservationId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(String.format("Failure to get Reservation with id: %d"
                        , reservationId), DaoErrorCode.RESERVATION_IS_NOT_FOUND));
    }

    @Override
    public List<Reservation> getAll() {
        return reservationsCache;
    }

    @Override
    public Set<ReservationPeriod> getAllReservationPeriodsByCoworking(Long coworkingId) {
        List<Reservation> coworkingSpaces = reservationsCache.stream()
                .filter(reservation -> reservation.getCoworkingSpace().getId().equals(coworkingId)).toList();
        return coworkingSpaces.stream()
                .map(Reservation::getPeriod)
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

    private boolean checkIfNotExist(Long id) {
        return reservationsCache.stream()
                .noneMatch(r -> r.getId().equals(id));
    }

    public void shutdown() {
        reservationLoader.save(reservationsCache);
    }

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
