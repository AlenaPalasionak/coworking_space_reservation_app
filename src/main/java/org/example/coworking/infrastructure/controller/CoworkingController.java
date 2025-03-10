package org.example.coworking.infrastructure.controller;

import org.example.coworking.infrastructure.controller.exception.PriceFormatException;
import org.example.coworking.infrastructure.dao.exception.CoworkingNotFoundException;
import org.example.coworking.infrastructure.util.InputValidator;
import org.example.coworking.model.CoworkingSpace;
import org.example.coworking.model.CoworkingType;
import org.example.coworking.model.Facility;
import org.example.coworking.model.User;
import org.example.coworking.service.CoworkingService;
import org.example.coworking.service.exception.ForbiddenActionException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.example.coworking.infrastructure.logger.Log.USER_OUTPUT_LOGGER;
import static org.example.coworking.infrastructure.logger.Log.TECHNICAL_LOGGER;

public class CoworkingController {

    private final CoworkingService coworkingService;
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

    public CoworkingController(CoworkingService coworkingService) {
        this.coworkingService = coworkingService;
    }

    public void add(BufferedReader reader, User admin) throws IOException {

        double price = 0;
        while (price == 0) {
            try {
                price = getPriceFromUser(reader);
            } catch (PriceFormatException e) {
                USER_OUTPUT_LOGGER.warn(e.getMessage() + ". Try again. ");
                TECHNICAL_LOGGER.warn(e.getMessage());
            }
        }
        CoworkingType coworkingType = getCoworkingTypeFromUser(reader);
        List<Facility> facilities = getFacilityFromUser(reader);

        coworkingService.add(admin, price, coworkingType, facilities);

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
                    int coworkingId = InputValidator.getIntInput(reader
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

    private double getPriceFromUser(BufferedReader reader) throws IOException, PriceFormatException {
        USER_OUTPUT_LOGGER.info("Enter the price in dollars per hour.\n");
        String price = reader.readLine();

        if (price == null || price.trim().isEmpty() || !price.matches("\\d+(\\.\\d+)?")) {
            throw new PriceFormatException(price);
        }

        return Double.parseDouble(price);
    }

    private CoworkingType getCoworkingTypeFromUser(BufferedReader reader) throws IOException {
        CoworkingType coworkingType = null;
        while (coworkingType == null) {
            try {
                USER_OUTPUT_LOGGER.info(COWORKING_TYPE_MENU);
                int coworkingTypeIndex = Integer.parseInt(reader.readLine());
                if (coworkingTypeIndex < 0 || coworkingTypeIndex >= CoworkingType.values().length) {
                    USER_OUTPUT_LOGGER.warn("You put a wrong symbol: " + coworkingTypeIndex + " Try again\n");
                    TECHNICAL_LOGGER.warn("Wrong symbol: " + coworkingTypeIndex);
                    continue;
                }
                coworkingType = CoworkingType.values()[coworkingTypeIndex];
            } catch (NumberFormatException e) {
                USER_OUTPUT_LOGGER.warn("You entered wrong number. Try again\n");
                TECHNICAL_LOGGER.warn("Wrong symbol");
            }
        }
        return coworkingType;
    }

    private List<Facility> getFacilityFromUser(BufferedReader reader) throws IOException {
        boolean areChosen = false;
        List<Facility> selectedFacilities = new ArrayList<>();
        while (!areChosen) {
            USER_OUTPUT_LOGGER.info(FACILITY_MENU);

            String facilitiesIndexesOnOneLine = reader.readLine();
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
        return selectedFacilities;
    }
}