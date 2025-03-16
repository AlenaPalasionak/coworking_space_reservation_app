package org.example.coworking.dao;

import org.example.coworking.dao.exception.ReservationNotFoundException;
import org.example.coworking.model.Reservation;

import java.util.List;

public interface ReservationDao extends ModelDao<Reservation, ReservationNotFoundException> {
    void add(Reservation reservation);
    void delete(Reservation reservation) throws ReservationNotFoundException;
    Reservation getById(Long reservationId) throws ReservationNotFoundException;
    List<Reservation> getAll();
    void load();
    void save();
}
