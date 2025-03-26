package org.example.coworking.dao;

import org.example.coworking.dao.exception.DaoErrorCode;
import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.loader.Loader;
import org.example.coworking.model.CoworkingSpace;
import org.example.coworking.model.Reservation;
import org.example.coworking.model.ReservationPeriod;

import java.sql.Connection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ReservationDaoImpl implements ReservationDao {
    private static List<Reservation> reservationsCache;
    private final Loader<Reservation> reservationLoader;

    public ReservationDaoImpl(Loader<Reservation> reservationLoader) {
        this.reservationLoader = reservationLoader;
    }

    @Override
    public void add(Reservation reservation) {
        boolean isUniqueIdGenerated;
        Long generatedId;
        do {
            generatedId = IdGenerator.generateReservationId();
            Long finalGeneratedId = generatedId;
            isUniqueIdGenerated = reservationsCache.stream()
                    .anyMatch(r -> r.getId().equals(finalGeneratedId));
        } while (isUniqueIdGenerated);

        reservation.setId(generatedId);
        addPeriodToCoworking(reservation.getPeriod(), reservation.getCoworkingSpace());
        reservationsCache.add(reservation);
    }

    @Override
    public void delete(Reservation reservation) throws EntityNotFoundException {
        if (checkIfNotExist(reservation.getId())) {
            throw new EntityNotFoundException("Reservation with id: " + reservation.getId() + " is not found. "
                    , DaoErrorCode.RESERVATION_IS_NOT_FOUND);
        }
        removePeriod(reservation.getPeriod(), reservation.getCoworkingSpace());
        reservationsCache.remove(reservation);
    }

    @Override
    public Reservation getById(Long reservationId) throws EntityNotFoundException {
        return reservationsCache.stream()
                .filter(r -> r.getId().equals(reservationId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Reservation with id: " + reservationId + " is not found. "
                        , DaoErrorCode.RESERVATION_IS_NOT_FOUND));
    }

    @Override
    public List<Reservation> getAll() {
        return reservationsCache;
    }

    private void addPeriodToCoworking(ReservationPeriod period, CoworkingSpace coworkingSpace) {
        boolean isUniqueIdGenerated;
        Long generatedId;
        do {
            generatedId = IdGenerator.generatePeriodId();
            Long finalGeneratedId = generatedId;
            Set<ReservationPeriod> periods = reservationsCache.stream()
                    .map(Reservation::getPeriod)
                    .collect(Collectors.toSet());

            isUniqueIdGenerated = periods.stream()
                    .anyMatch(p -> p.getId().equals(finalGeneratedId));
        } while (isUniqueIdGenerated);
        period.setId(generatedId);
        coworkingSpace.getReservationsPeriods().add(period);
    }

    private void removePeriod(ReservationPeriod period, CoworkingSpace coworkingSpace) {
        coworkingSpace.getReservationsPeriods().remove(period);
    }

    private boolean checkIfNotExist(Long id) {
        return reservationsCache.stream()
                .noneMatch(r -> r.getId().equals(id));
    }

    @Override
    public Reservation getById(Long reservationId, Connection connection) throws EntityNotFoundException {
        throw new UnsupportedOperationException("Use method public Reservation getById(Long reservationId)");

    }
}
