package org.example.coworking.infrastructure.dao;

import org.example.coworking.infrastructure.dao.exception.ReservationNotFoundException;
import org.example.coworking.infrastructure.loader.Loader;
import org.example.coworking.model.Reservation;

import java.io.FileNotFoundException;
import java.util.List;

import static org.example.coworking.infrastructure.logger.Log.TECHNICAL_LOGGER;

public class ReservationDaoImpl implements ReservationDao {
    private static List<Reservation> reservationsCache;
    private final Loader<Reservation> reservationLoader;

    public ReservationDaoImpl(Loader<Reservation> reservationLoader) {
        this.reservationLoader = reservationLoader;
    }

    @Override
    public void load() {
        if (reservationsCache == null) {
            reservationsCache = getFromStorage();
        }
    }

    @Override
    public void save() {
        reservationLoader.save(reservationsCache);
    }

    @Override
    public void add(Reservation reservation) {
        boolean isUniqueIdGenerated;
        long generatedId;
        do {
            generatedId = IdGenerator.generateReservationId();
            long finalGeneratedId = generatedId;
            isUniqueIdGenerated = reservationsCache.stream()
                    .anyMatch(r -> r.getId() == finalGeneratedId);
        } while (isUniqueIdGenerated);

        reservation.setId(generatedId);
        reservation.getCoworkingSpace().getReservationsPeriods().add(reservation.getPeriod());
        reservationsCache.add(reservation);
    }

    @Override
    public void delete(Reservation reservation) throws ReservationNotFoundException {
        if (checkIfNotExist(reservation.getId())) {
            throw new ReservationNotFoundException("Reservation with id: " + reservation.getId() + " is not found. ");
        }
        reservationsCache.remove(reservation);
        reservation.getCoworkingSpace().getReservationsPeriods().remove(reservation.getPeriod());
    }

    public Reservation getById(int reservationId) throws ReservationNotFoundException {
        return reservationsCache.stream()
                .filter(r -> r.getId() == reservationId)
                .findFirst()
                .orElseThrow(() -> new ReservationNotFoundException("Reservation with id: " + reservationId + " is not found. "));
    }

    @Override
    public List<Reservation> getAll() {
        return reservationsCache;
    }

    private boolean checkIfNotExist(long id) {
        return reservationsCache.stream()
                .noneMatch(r -> r.getId() == id);
    }

    private List<Reservation> getFromStorage() {
        try {
            reservationsCache = reservationLoader.load(Reservation.class);
        } catch (FileNotFoundException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return reservationsCache;
    }
}
