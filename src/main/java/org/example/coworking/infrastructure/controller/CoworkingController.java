package org.example.coworking.infrastructure.controller;

import org.example.coworking.controller.InputFormatException;
import org.example.coworking.infrastructure.controller.exception.CoworkingTypeNotFoundException;
import org.example.coworking.infrastructure.controller.exception.PriceFormatException;
import org.example.coworking.infrastructure.controller.validator.InputValidator;
import org.example.coworking.infrastructure.dao.exception.CoworkingNotFoundException;
import org.example.coworking.infrastructure.mapper.CoworkingMapper;
import org.example.coworking.model.CoworkingSpace;
import org.example.coworking.model.CoworkingType;
import org.example.coworking.model.Facility;
import org.example.coworking.model.User;
import org.example.coworking.service.CoworkingService;
import org.example.coworking.service.exception.ForbiddenActionException;
import org.example.coworking.service.util.InputSupplierCreator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    private InputSupplierCreator getInputStringSupplier(BufferedReader reader) {

        return new InputSupplierCreator(
                USER_OUTPUT_LOGGER::info,
                () -> {
                    String i;
                    try {
                        i = reader.readLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    return i;
                }
        );
    }

    public void add(BufferedReader reader, User admin) throws IOException {
        double price;
        CoworkingType coworkingType = null;
        List<Facility> selectedFacilities;
        while (true) {
            String priceInput = getInputStringSupplier(reader).supplier("Enter the price in dollars per hour.\n");
            try {
                if (InputValidator.isPriceValid(priceInput)) {
                    price = coworkingMapper.getPrice(priceInput);
                    break;
                }
            } catch (PriceFormatException e) {
                USER_OUTPUT_LOGGER.warn(e.getMessage() + ". Try again. ");
                TECHNICAL_LOGGER.warn(e.getMessage());
            }
        }

        while (coworkingType == null) {
            String coworkingTypeInput = getInputStringSupplier(reader).supplier(COWORKING_TYPE_MENU);
            if (InputValidator.isNumber(coworkingTypeInput)) {
                try {
                    coworkingType = coworkingMapper.getCoworkingType(coworkingTypeInput);
                } catch (InputFormatException | CoworkingTypeNotFoundException e) {
                    USER_OUTPUT_LOGGER.warn(e.getMessage());
                    TECHNICAL_LOGGER.warn(e.getMessage());
                }
            }
        }

        boolean areChosen = false;
        while (!areChosen) {
            String facilitiesIndexesOnOneLine = getInputStringSupplier(reader).supplier(FACILITY_MENU);
            if (facilitiesIndexesOnOneLine.matches("")) {
                areChosen = true;
            } else if (InputValidator.isFacilityStringFromUserValid(facilitiesIndexesOnOneLine)) {
                String[] facilitiesIndexes = facilitiesIndexesOnOneLine.split(",");
                Arrays.sort(facilitiesIndexes);
                facilitiesIndexes = Arrays.stream(facilitiesIndexes).distinct().toArray(String[]::new);
                for (String facilitiesIndex : facilitiesIndexes) {
                    int facilityIndex = Integer.parseInt(facilitiesIndex.trim());
                    if (facilityIndex >= 0 && facilityIndex < Facility.values().length) {
                        selectedFacilities.add(Facility.values()[facilityIndex]);
                        areChosen = true;
                    }
                }
            } else {
                USER_OUTPUT_LOGGER.warn("You entered wrong number(s): " + facilitiesIndexesOnOneLine + ". Try again:\n");
                TECHNICAL_LOGGER.warn("wrong number(s): " + facilitiesIndexesOnOneLine);
            }
        }

        coworkingService.add(new CoworkingSpace());
        USER_OUTPUT_LOGGER.info("You just added a new Space:\n");
    }

    public void delete(BufferedReader reader, User user) throws IOException {
        List<CoworkingSpace> spaces = coworkingService.getAllByUser(user);
        boolean isDeleted = false;
        if (spaces.isEmpty()) {
            USER_OUTPUT_LOGGER.warn("Coworking List is empty\n");
            TECHNICAL_LOGGER.warn("Coworking List is empty\n");
        } else {
            while (!isDeleted) {
                spaces.forEach(space -> USER_OUTPUT_LOGGER.info(space.toString()));
                try {
                    int coworkingId = InputValidator.convertInputToInt(reader
                            , "Type a coworking id you want to delete:\n");
                    Optional<CoworkingSpace> possibleCoworking;

                    possibleCoworking = coworkingService.getById(coworkingId);
                    if (possibleCoworking.isPresent()) {
                        CoworkingSpace coworkingSpace = possibleCoworking.get();
                        coworkingService.delete(user, coworkingSpace);
                        USER_OUTPUT_LOGGER.info("Coworking with id: " + coworkingId + " has been deleted\n");
                        isDeleted = true;
                    }
                } catch (CoworkingNotFoundException e) {
                    USER_OUTPUT_LOGGER.info(e.getMessage() + "\n" + "Choose another Coworking \n");
                } catch (ForbiddenActionException e) {
                    USER_OUTPUT_LOGGER.error(e.getMessage() + "\n");
                    TECHNICAL_LOGGER.error(e.getMessage());
                }
            }
        }
    }

    public void getAllSpaces(User user) {
        List<CoworkingSpace> spaces = coworkingService.getAllByUser(user);
        USER_OUTPUT_LOGGER.info("Spaces list:\n");
        spaces.forEach(space -> USER_OUTPUT_LOGGER.info(space.toString()));
    }

    public void load() {
        coworkingService.load();
    }

    public void save() {
        coworkingService.save();
    }
}