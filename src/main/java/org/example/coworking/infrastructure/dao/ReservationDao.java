package org.example.coworking.infrastructure.dao;

import org.example.coworking.infrastructure.dao.exception.ReservationNotFoundException;
import org.example.coworking.model.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationDao extends ModelDao<Reservation, ReservationNotFoundException> {
    void add(Reservation reservation);
    void delete(Reservation reservation) throws ReservationNotFoundException;
    Optional<Reservation> getById(int reservationId) throws ReservationNotFoundException;
    List<Reservation> getAll();
    void load();
    void save();

}
