package org.example.coworking.infrastructure.controller;

import org.example.coworking.infrastructure.controller.exception.InvalidInputException;
import org.example.coworking.infrastructure.controller.validator.InputValidator;
import org.example.coworking.infrastructure.dao.exception.CoworkingNotFoundException;
import org.example.coworking.infrastructure.mapper.CoworkingMapper;
import org.example.coworking.infrastructure.mapper.exception.CoworkingTypeIndexException;
import org.example.coworking.infrastructure.mapper.exception.FacilityIndexException;
import org.example.coworking.model.CoworkingSpace;
import org.example.coworking.model.CoworkingType;
import org.example.coworking.model.Facility;
import org.example.coworking.model.User;
import org.example.coworking.service.CoworkingService;
import org.example.coworking.service.exception.ForbiddenActionException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import static org.example.coworking.infrastructure.logger.Log.TECHNICAL_LOGGER;
import static org.example.coworking.infrastructure.logger.Log.USER_OUTPUT_LOGGER;

public class CoworkingController {

    private final CoworkingService coworkingService;
    private final CoworkingMapper coworkingMapper;
    public static final String COWORKING_TYPE_MENU = """
            Choose the coworkingSpace type (press only one of the numbers):
            Open space coworkingSpace - 0
            Private Office - 1
            Coworking+Co-living - 2
            """;
    public static final String FACILITY_MENU = """
            Choose the facilities. Write numbers comma-separated on one line:
            NO Facilities - just press Enter,
            PARKING - 0,
            WIFI - 1,
            KITCHEN - 2,
            PRINTER - 3,
            CONDITIONING - 4
            """;

    public CoworkingController(CoworkingService coworkingService, CoworkingMapper coworkingMapper) {
        this.coworkingService = coworkingService;
        this.coworkingMapper = coworkingMapper;
    }

    public void load() {
        coworkingService.load();
    }

    public void save() {
        coworkingService.save();
    }

    public void add(BufferedReader reader, User admin) throws IOException {
        double price;
        CoworkingType coworkingType;
        List<Facility> facilities;
        while (true) {
            String priceInput;
            try {
                priceInput = InputValidator.getInputSupplier(reader, "\\d+(\\.\\d+)?")
                        .supplier("Enter the price in dollars per hour.\n");
            } catch (InvalidInputException e) {
                USER_OUTPUT_LOGGER.warn(e.getMessage() + " Try again:\n");
                TECHNICAL_LOGGER.warn(e.getMessage());
                continue;
            }

            price = coworkingMapper.getPrice(priceInput);
            break;
        }

        while (true) {
            String coworkingTypeInput;
            try {
                coworkingTypeInput = InputValidator.getInputSupplier(reader, "\\d+")
                        .supplier(COWORKING_TYPE_MENU);
            } catch (InvalidInputException e) {
                USER_OUTPUT_LOGGER.warn(e.getMessage() + " Try again:\n");
                TECHNICAL_LOGGER.warn(e.getMessage());
                continue;
            }
            try {
                coworkingType = coworkingMapper.getCoworkingType(coworkingTypeInput);
                break;
            } catch (CoworkingTypeIndexException e) {
                USER_OUTPUT_LOGGER.warn(e.getMessage() + " Try again:\n");
                TECHNICAL_LOGGER.warn(e.getMessage());
            }
        }

        while (true) {
            String facilitiesIndexesInput = null;
            try {
                facilitiesIndexesInput = InputValidator.getInputSupplier(reader, "^\\s*\\d+(\\s*,\\s*\\d+)*\\s*$")
                        .supplier(FACILITY_MENU + "\nPlease enter numbers separated by commas.\n");
            } catch (InvalidInputException e) {
                USER_OUTPUT_LOGGER.warn(e.getMessage() + ". Try again:\n");
                TECHNICAL_LOGGER.warn(e.getMessage() + facilitiesIndexesInput);
                continue;
            }
            try {
                facilities = coworkingMapper.getFacility(facilitiesIndexesInput);
                break;
            } catch (FacilityIndexException e) {
                USER_OUTPUT_LOGGER.warn(e.getMessage() + ". Try again:\n");
                TECHNICAL_LOGGER.warn(e.getMessage());
            }
        }

        coworkingService.add(admin, price, coworkingType, facilities);
        USER_OUTPUT_LOGGER.info("You just added a new Space:\n");
    }

    public void delete(BufferedReader reader, User user) throws IOException {
        List<CoworkingSpace> coworkingSpaces = coworkingService.getAllByUser(user);
        boolean isDeleted = false;
        String spacesAsString = coworkingSpaces.stream()
                .map(CoworkingSpace::toString)
                .reduce((s1, s2) -> s1 + "\n" + s2)
                .orElse("No spaces available");
        while (!isDeleted) {
            String coworkingIdInput;
            try {
                coworkingIdInput = InputValidator.getInputSupplier(reader, "\\d+")
                        .supplier(spacesAsString + "Type a coworking id you want to delete:\n");
            } catch (InvalidInputException e) {
                USER_OUTPUT_LOGGER.warn(e.getMessage() + ". Try again:\n");
                TECHNICAL_LOGGER.warn(e.getMessage());
                continue;
            }
            try {
                int coworkingSpaceId = Integer.parseInt(coworkingIdInput);
                coworkingService.delete(user, coworkingSpaceId);
                USER_OUTPUT_LOGGER.info("Coworking with id: " + coworkingSpaceId + " has been deleted\n");
                isDeleted = true;
            } catch (CoworkingNotFoundException | ForbiddenActionException e) {
                USER_OUTPUT_LOGGER.warn(e.getMessage() + "\n" + "Try again\n");
                TECHNICAL_LOGGER.warn(e.getMessage());
            }
        }
    }

    public void getAllSpaces(User user) {
        List<CoworkingSpace> spaces = coworkingService.getAllByUser(user);
        USER_OUTPUT_LOGGER.info("Spaces list:\n");
        spaces.forEach(space -> USER_OUTPUT_LOGGER.info(space.toString()));
    }
}