package org.example.coworking.infrastructure.dao;

import org.apache.logging.log4j.Logger;
import org.example.coworking.infrastructure.dao.exception.ReservationNotFoundException;
import org.example.coworking.infrastructure.json_loader.Loader;
import org.example.coworking.infrastructure.logger.Log;
import org.example.coworking.model.Reservation;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReservationDaoImpl implements ReservationDao {
    private static final Logger logger = Log.getLogger(ReservationDaoImpl.class);
    private static List<Reservation> reservationsCache;
    private final Loader reservationLoader;

    public ReservationDaoImpl(Loader reservationLoader) {
        this.reservationLoader = reservationLoader;
    }

    @Override
    public void addReservation(Reservation reservation) {
        boolean isUniqueIdGenerated;
        int generatedId;
        do {
            generatedId = IdGenerator.generateReservationId();
            isUniqueIdGenerated = true;

            for (Reservation reservationFromCache : reservationsCache) {
                if (reservationFromCache.getId() == generatedId) {
                    isUniqueIdGenerated = false;
                    break;
                }
            }
        } while (!isUniqueIdGenerated);

        reservation.setId(generatedId);
        reservation.getCoworkingSpace().getReservationsPeriods().add(reservation.getPeriod());
        reservationsCache.add(reservation);
    }

    @Override
    public void delete(Reservation reservation) throws ReservationNotFoundException {
        if (checkIfNotExist(reservation.getId())) {
            throw new ReservationNotFoundException(reservation.getId());
        }
        reservationsCache.remove(reservation);
        reservation.getCoworkingSpace().getReservationsPeriods().remove(reservation.getPeriod());
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationsCache;
    }

    public Optional<Reservation> getReservationById(int reservationId) throws ReservationNotFoundException {
        Optional<Reservation> possibleReservation;
        if (checkIfNotExist(reservationId)) {
            throw new ReservationNotFoundException(reservationId);
        } else {
            possibleReservation = reservationsCache.stream().filter(r -> r.getId() == reservationId).findFirst();
        }
        return possibleReservation;
    }

    @Override
    public List<Reservation> getReservationsByCustomer(int customerId) {
        return reservationsCache.stream().filter(reservation -> reservation.getCustomer().getId() == customerId).collect(Collectors.toList());
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

    private boolean checkIfNotExist(int id) {
        return reservationsCache.stream().noneMatch(c -> c.getId() == id);
    }

    private List<Reservation> getFromStorage() {
        try {
            reservationsCache = reservationLoader.load(Reservation.class);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return reservationsCache;
    }
}
