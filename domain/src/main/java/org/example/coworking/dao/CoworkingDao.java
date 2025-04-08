package org.example.coworking.dao;

import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.model.CoworkingSpace;

import java.util.List;

/**
 * This interface defines the data access operations for the {@link CoworkingSpace} model.
 * It extends the {@link ModelDao} interface to provide basic CRUD operations with
 * {@link EntityNotFoundException} as the exception type for errors.
 * It provides additional methods for adding, deleting, retrieving, and saving coworking space data.
 */
public interface CoworkingDao extends ModelDao<CoworkingSpace> {

    /**
     * Retrieves all coworking spaces managed by a specific admin.
     *
     * @param adminId the ID of the admin
     * @return a list of {@code CoworkingSpace} objects managed by the admin
     */
    List<CoworkingSpace> getAllCoworkingSpacesByAdmin(Long adminId);

    /**
     * Retrieves the admin ID associated with a specific coworking space.
     * <p>
     * This method returns the ID of the admin who manages the coworking space identified
     * by the given coworking space ID. If the coworking space is not found, an exception
     * will be thrown.
     *
     * @param coworkingSpaceId the ID of the coworking space for which the admin ID is to be retrieved.
     * @return the ID of the admin who manages the specified coworking space.
     * @throws EntityNotFoundException if no coworking space with the given ID exists.
     */
    Long getAdminIdByCoworkingSpaceId(Long coworkingSpaceId) throws EntityNotFoundException;

}
