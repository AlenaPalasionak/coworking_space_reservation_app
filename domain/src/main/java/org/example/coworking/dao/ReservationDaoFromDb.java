package org.example.coworking.dao;

import org.example.coworking.dao.exception.ReservationNotFoundException;
import org.example.coworking.model.Reservation;

import java.util.List;


public interface ReservationDaoFromDb extends Dao<Reservation, ReservationNotFoundException> {

    void add(Reservation object);

    void delete(Reservation object) throws ReservationNotFoundException;

    Reservation getById(Long id) throws ReservationNotFoundException;

    List<Reservation> getAll();

    void update(Reservation reservation);
}

