package org.example.coworking.controller;

import org.example.coworking.controller.exception.InvalidInputException;
import org.example.coworking.controller.validator.InputValidator;
import org.example.coworking.repository.exception.EntityNotFoundException;
import org.example.coworking.mapper.CoworkingMapper;
import org.example.coworking.entity.*;
import org.example.coworking.entity.exception.CoworkingTypeIndexException;
import org.example.coworking.entity.exception.FacilityTypeIndexException;
import org.example.coworking.service.CoworkingService;
import org.example.coworking.service.exception.ForbiddenActionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.util.List;
import java.util.Set;

import static org.example.coworking.logger.Log.TECHNICAL_LOGGER;
import static org.example.coworking.logger.Log.USER_OUTPUT_LOGGER;

/**
 * This class handles user interactions related to coworking spaces. It interacts with the {@link CoworkingService}
 * and {@link CoworkingMapper} to load, save, add, delete, and retrieve coworking spaces.
 * It also provides menus for selecting coworking types and facilities and validates user inputs.
 */
@Component
public class CoworkingController {
    private final CoworkingService coworkingService;
    private final CoworkingMapper coworkingMapper;

    public static final String COWORKING_TYPE_OPTIONS = """
            Choose the coworkingSpace type (press only one of the numbers):
            Open Space - 0
            Private Office - 1
            Co Living - 2
            """;

    public static final String FACILITY_OPTIONS = """
            Choose the facilities. Write numbers comma-separated on one line:
            NO Facilities - just press Enter,
            PARKING - 0,
            WIFI - 1,
            KITCHEN - 2,
            PRINTER - 3,
            CONDITIONING - 4
            """;

    private static final String ANY_NUMBER_PATTERN = "\\d+";
    private static final String FACILITY_PATTERN = "^\\s*(\\d+(\\s*,\\s*\\d+)*)?\\s*$";
    private static final String PRICE_PATTERN = "\\d+(\\.\\d+)?";

    /**
     * Constructs a {@link CoworkingController} with the specified {@link CoworkingService} and {@link CoworkingMapper}.
     *
     * @param coworkingService the service for managing coworking spaces
     * @param coworkingMapper  the mapper used for mapping inputs to valid coworking types and facilities
     */
    @Autowired
    public CoworkingController(CoworkingService coworkingService, CoworkingMapper coworkingMapper) {
        this.coworkingService = coworkingService;
        this.coworkingMapper = coworkingMapper;
    }

    /**
     * Prompts the admin to enter details to add a new coworking space, such as price, coworking type, and facilities.
     * After the input is validated, the coworking space is added using the {@link CoworkingService}.
     *
     * @param reader the {@link BufferedReader} used to get user input
     * @param admin  the admin user adding the coworking space
     */
    public void add(BufferedReader reader, Admin admin) {
        double price;
        CoworkingType coworkingType;
        Set<Facility> facilities;

        while (true) {
            String priceInput;
            try {
                priceInput = InputValidator.getInputSupplier(reader, PRICE_PATTERN)
                        .supplier("Enter the price in dollars per hour.\n");
                price = coworkingMapper.getPrice(priceInput);
                break;
            } catch (InvalidInputException e) {
                USER_OUTPUT_LOGGER.warn(e.getErrorCode());
                TECHNICAL_LOGGER.warn(e.getMessage());
            }
        }

        while (true) {
            String coworkingTypeInput;
            try {
                coworkingTypeInput = InputValidator.getInputSupplier(reader, ANY_NUMBER_PATTERN)
                        .supplier(COWORKING_TYPE_OPTIONS);
                coworkingType = coworkingMapper.getCoworkingType(coworkingTypeInput);
                break;
            } catch (InvalidInputException e) {
                USER_OUTPUT_LOGGER.warn(e.getErrorCode());
                TECHNICAL_LOGGER.warn(e.getMessage());
            } catch (CoworkingTypeIndexException e) {
                USER_OUTPUT_LOGGER.warn(e.getErrorCode());
                TECHNICAL_LOGGER.warn(e.getMessage());
            }
        }

        while (true) {
            String facilityCodesInput;
            try {
                facilityCodesInput = InputValidator.getInputSupplier(reader, FACILITY_PATTERN)
                        .supplier(FACILITY_OPTIONS + "\nPlease enter numbers separated by commas.\n");
                facilities = coworkingMapper.getFacility(facilityCodesInput);
                break;
            } catch (InvalidInputException e) {
                USER_OUTPUT_LOGGER.warn(e.getErrorCode());
                TECHNICAL_LOGGER.warn(e.getMessage());
            } catch (FacilityTypeIndexException e) {
                USER_OUTPUT_LOGGER.warn(e.getErrorCode());
                TECHNICAL_LOGGER.warn(e.getMessage());
            }
        }

        coworkingService.add(admin, price, coworkingType, facilities);
        USER_OUTPUT_LOGGER.info("You just added a new Space:\n");
    }

    /**
     * Prompts the user to select a coworking space from their list and delete it.
     * The coworking space is deleted using the {@link CoworkingService}.
     *
     * @param reader the {@link BufferedReader} used to get user input
     * @param admin  the user deleting the coworking space
     */
    public void delete(BufferedReader reader, Admin admin) {
        List<CoworkingSpace> coworkingSpaces = coworkingService.getAllByAdmin(admin);
        if (coworkingSpaces.isEmpty()) {
            USER_OUTPUT_LOGGER.info("Coworking Spaces list is empty.\n");
            return;
        }

        String spacesAsString = coworkingSpaces.stream()
                .map(CoworkingSpace::toString)
                .reduce((s1, s2) -> s1 + "\n" + s2)
                .orElse("");

        while (true) {
            String coworkingIdInput;
            try {
                coworkingIdInput = InputValidator.getInputSupplier(reader, ANY_NUMBER_PATTERN)
                        .supplier(spacesAsString + "Type a coworking id you want to delete:\n");
                Long coworkingSpaceId = Long.parseLong(coworkingIdInput);
                coworkingService.delete(admin, coworkingSpaceId);
                USER_OUTPUT_LOGGER.info("Coworking with id: " + coworkingSpaceId + " has been deleted\n");
                break;
            } catch (InvalidInputException e) {
                USER_OUTPUT_LOGGER.warn(e.getErrorCode());
                TECHNICAL_LOGGER.warn(e.getMessage());
            } catch (ForbiddenActionException e) {
                USER_OUTPUT_LOGGER.warn(e.getErrorCode());
                TECHNICAL_LOGGER.warn(e.getMessage());
            } catch (EntityNotFoundException e) {
                USER_OUTPUT_LOGGER.warn(e.getErrorCode());
                TECHNICAL_LOGGER.warn(e.getMessage());
            }
        }
    }

    /**
     * Retrieves and displays all coworking spaces.
     */
    public void getAllSpaces() {
        List<CoworkingSpace> spaces = coworkingService.getAll();
        USER_OUTPUT_LOGGER.info("Spaces list:\n");
        spaces.forEach(space -> USER_OUTPUT_LOGGER.info(space.toString()));
    }
}
