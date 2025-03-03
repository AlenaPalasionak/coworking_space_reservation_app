package org.example.coworking.infrastructure.dao;

import org.example.coworking.infrastructure.json_loader.JsonLoader;
import org.example.coworking.model.Reservation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReservationDaoImpl implements ReservationDao {
    private static List<Reservation> reservationsCache;
    private final JsonLoader reservationLoader;

    public ReservationDaoImpl(JsonLoader reservationLoader) {
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


        reservation.setId(IdGenerator.generateReservationId());
            reservation.getCoworkingSpace().getReservationsPeriods().add(reservation.getPeriod());
            reservationsCache.add(reservation);
        }


    @Override
    public void delete(Reservation reservation) {
        reservation.getCoworkingSpace().getReservationsPeriods().remove(reservation.getPeriod());
        reservationsCache.remove(reservation);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationsCache;
    }

    public Optional<Reservation> getReservationById(int reservationId) {
        for (Reservation reservation : reservationsCache) {
            if (reservation.getId() == reservationId) {
                return Optional.of(reservation);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Reservation> getReservationsByCustomer(int customerId) {
        return reservationsCache.stream().filter(reservation -> reservation.getCustomer().getId() == customerId).collect(Collectors.toList());
    }

    @Override
    public void getReservationsFromJson() {
        if (reservationsCache == null) {
            reservationsCache = getFromJson();
        }
    }

    @Override
    public void saveToJSON() {
        reservationLoader.convertToJson(reservationsCache);
    }

    private List<Reservation> getFromJson() {
        reservationsCache = reservationLoader.loadFromJson(Reservation.class);
        return reservationsCache;
    }
}
