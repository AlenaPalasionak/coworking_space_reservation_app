package org.example.coworking.dao;

import org.example.coworking.dao.exception.DaoErrorCode;
import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.loader.Loader;
import org.example.coworking.model.CoworkingSpace;
import org.example.coworking.model.Reservation;
import org.example.coworking.model.ReservationPeriod;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class FileReservationDao implements ReservationDao {
    private static List<Reservation> reservationsCache;
    private final Loader<Reservation> reservationLoader;

    public FileReservationDao(Loader<Reservation> reservationLoader) {
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
        reservationsCache.add(reservation);
        addReservationToCoworking(reservation, reservation.getCoworkingSpace());
    }

    @Override
    public void delete(Reservation reservation) throws EntityNotFoundException {
        if (checkIfNotExist(reservation.getId())) {
            throw new EntityNotFoundException("Reservation with id: " + reservation.getId() + " is not found. "
                    , DaoErrorCode.RESERVATION_IS_NOT_FOUND);
        }
        removeReservationFromCoworking(reservation, reservation.getCoworkingSpace());
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

    private void removeReservationFromCoworking(Reservation reservation, CoworkingSpace coworkingSpace) throws EntityNotFoundException {
        Optional<Reservation> possibleReservation = coworkingSpace.getReservations().stream()
                .filter(r -> r.equals(reservation))
                .findFirst();
        if (possibleReservation.isPresent())
            coworkingSpace.getReservations().remove(possibleReservation.get());
        else throw new EntityNotFoundException("Failure to find reservation with id : " + reservation.getId()
                , DaoErrorCode.RESERVATION_IS_NOT_FOUND);
    }

    private void addReservationToCoworking(Reservation reservation, CoworkingSpace coworkingSpace) {
        coworkingSpace.getReservations().add(reservation);
    }

    private boolean checkIfNotExist(Long id) {
        return reservationsCache.stream()
                .noneMatch(r -> r.getId().equals(id));
    }

    @Override
    public Set<ReservationPeriod> getAllReservationPeriodsByCoworking(Long coworkingId) {
        List<Reservation> coworkingSpaces = reservationsCache.stream()
                .filter(reservation -> reservation.getCoworkingSpace().getId().equals(coworkingId))
                .collect(Collectors.toList());
        return coworkingSpaces.stream()
                .map(reservation -> reservation.getPeriod())
                .collect(Collectors.toSet());

    }

    @Override
    public List<Reservation> getAllReservationsByCustomer(Long customerId) {
        return reservationsCache.stream()
                .filter(reservation -> reservation.getCoworkingSpace().getAdmin().getId().equals(customerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> getAllReservationsByAdmin(Long adminId) {
        return reservationsCache.stream()
                .filter(reservation -> reservation.getCustomer().equals(adminId))
                .collect(Collectors.toList());
    }
}
