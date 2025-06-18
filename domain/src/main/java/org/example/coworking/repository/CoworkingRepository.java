package org.example.coworking.repository;

import org.example.coworking.repository.exception.EntityNotFoundException;
import org.example.coworking.entity.CoworkingSpace;

import java.util.List;

/**
 * This interface defines the data access operations for the {@link CoworkingSpace} model.
 * It extends the {@link ModelRepository} interface to provide basic CRUD operations with
 * {@link EntityNotFoundException} as the exception type for errors.
 * It provides additional methods for adding, deleting, retrieving, and saving coworking space data.
 */
public interface CoworkingRepository extends ModelRepository<CoworkingSpace> {

    /**
     * Retrieves all coworking spaces managed by a specific admin.
     *
     * @param adminId the ID of the admin
     * @return a list of {@code CoworkingSpace} objects managed by the admin
     */
    List<CoworkingSpace> getAllCoworkingSpacesByAdmin(Long adminId);
}
