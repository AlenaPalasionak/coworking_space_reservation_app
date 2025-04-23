package org.example.coworking.service;

import org.example.coworking.repository.exception.EntityNotFoundException;
import org.example.coworking.entity.Admin;
import org.example.coworking.entity.CoworkingSpace;
import org.example.coworking.service.exception.ForbiddenActionException;

import java.util.List;

/**
 * This interface defines the operations for managing coworking spaces in the system.
 * It allows for loading, saving, adding, deleting, and retrieving coworking spaces associated with users.
 */
public interface CoworkingService {

    /**
     * Adds a new coworking space for the specified user with the given price, coworking type, and list of facilities.
     * @param coworkingSpace to be added
     */
    void add(CoworkingSpace coworkingSpace);

    /**
     * Deletes a coworking space for the specified user by its ID.
     *
     * @param user             the user who is deleting the coworking space
     * @param coworkingSpaceId the ID of the coworking space to be deleted
     * @throws ForbiddenActionException if the user is not allowed to delete the coworking space
     * @throws EntityNotFoundException  if the coworking space with the given ID is not found
     */
    void delete(Admin user, Long coworkingSpaceId) throws ForbiddenActionException, EntityNotFoundException;

    /**
     * Retrieves all coworking spaces associated with the specified user.
     *
     * @return a list of coworking spaces
     */
    List<CoworkingSpace> getAll();

    /**
     * Retrieves all coworking spaces associated with the specified user.
     *
     * @param admin the user whose coworking spaces are to be retrieved
     * @return a list of coworking spaces associated with the user
     */
    List<CoworkingSpace> getAllByAdmin(Admin admin);

    /**
     * Retrieves a coworking space by its ID.
     *
     * @param id the ID of the coworking space to be retrieved
     * @return the coworking space associated with the given ID
     * @throws EntityNotFoundException if no coworking space with the given ID is found
     */
    CoworkingSpace getById(Long id) throws EntityNotFoundException;
}
