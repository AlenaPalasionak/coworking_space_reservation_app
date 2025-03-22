package org.example.coworking.service;

import org.example.coworking.dao.exception.CoworkingNotFoundException;
import org.example.coworking.model.*;
import org.example.coworking.service.exception.ForbiddenActionException;

import java.util.List;
import java.util.TreeSet;

/**
 * This interface defines the operations for managing coworking spaces in the system.
 * It allows for loading, saving, adding, deleting, and retrieving coworking spaces associated with users.
 */
public interface CoworkingService {

    /**
     * Loads the coworking spaces from a data source.
     */
    void load();

    /**
     * Saves the current state of coworking spaces to a data source.
     */
    void save();

    /**
     * Adds a new coworking space for the specified user with the given price, coworking type, and list of facilities.
     *
     * @param admin         the user who is adding the coworking space
     * @param price         the price of the coworking space
     * @param coworkingType the type of the coworking space
     * @param facilities    a list of facilities available in the coworking space
     */
    void add(User admin, double price, CoworkingType coworkingType, List<Facility> facilities);

    /**
     * Deletes a coworking space for the specified user by its ID.
     *
     * @param user the user who is deleting the coworking space
     * @param coworkingSpaceId the ID of the coworking space to be deleted
     * @throws ForbiddenActionException   if the user is not allowed to delete the coworking space
     * @throws CoworkingNotFoundException if the coworking space with the given ID is not found
     */
    void delete(User user, Long coworkingSpaceId) throws ForbiddenActionException, CoworkingNotFoundException;

    /**
     * Retrieves all coworking spaces associated with the specified user.
     *
     * @param user the user whose coworking spaces are to be retrieved
     * @return a list of coworking spaces associated with the user
     */
    List<CoworkingSpace> getAllByUser(User user);

    /**
     * Retrieves a coworking space by its ID.
     *
     * @param id the ID of the coworking space to be retrieved
     * @return the coworking space associated with the given ID
     * @throws CoworkingNotFoundException if no coworking space with the given ID is found
     */
    CoworkingSpace getById(Long id) throws CoworkingNotFoundException;

    TreeSet<ReservationPeriod> getCoworkingSpacePeriod(CoworkingSpace coworkingSpace);
}
