package org.example.coworking.dao;

import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.model.CoworkingSpace;
import org.example.coworking.model.Reservation;
import org.example.coworking.model.ReservationPeriod;

/**
 * This interface defines the data access operations for the {@link Reservation} model.
 * It extends the {@link Dao} interface to provide basic CRUD operations with
 * {@link EntityNotFoundException} as the exception type for errors.
 * It provides additional methods for adding, deleting, retrieving, and saving reservation data.
 */
public interface ReservationDao extends Dao<Reservation> {

    void addPeriodToCoworking(ReservationPeriod period, CoworkingSpace coworkingSpace);


}

