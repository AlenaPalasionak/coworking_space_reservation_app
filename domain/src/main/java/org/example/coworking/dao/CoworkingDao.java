package org.example.coworking.dao;

import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.model.CoworkingSpace;
/**
 * This interface defines the data access operations for the {@link CoworkingSpace} model.
 * It extends the {@link Dao} interface to provide basic CRUD operations with
 * {@link EntityNotFoundException} as the exception type for errors.
 * It provides additional methods for adding, deleting, retrieving, and saving coworking space data.
 */
public interface CoworkingDao extends Dao<CoworkingSpace> {

}
