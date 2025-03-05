package org.example.coworking.infrastructure.controller;

import org.apache.logging.log4j.Logger;
import org.example.coworking.infrastructure.controller.exception.PriceFormatException;
import org.example.coworking.infrastructure.dao.exception.CoworkingNotFoundException;
import org.example.coworking.infrastructure.logger.Log;
import org.example.coworking.infrastructure.util.StringHandler;
import org.example.coworking.model.CoworkingSpace;
import org.example.coworking.model.CoworkingType;
import org.example.coworking.model.Facility;
import org.example.coworking.model.User;
import org.example.coworking.service.CoworkingService;
import org.example.coworking.service.ReservationService;
import org.example.coworking.service.exception.ForbiddenActionException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CoworkingController {
    private static final Logger logger = Log.getLogger(CoworkingController.class);

    CoworkingService coworkingService;
    ReservationService reservationService;
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

    public CoworkingController(CoworkingService coworkingService, ReservationService reservationService) {
        this.coworkingService = coworkingService;
        this.reservationService = reservationService;
    }

    public void add(BufferedReader reader, BufferedWriter writer, User admin) throws IOException {

        double price = 0;
        while (price == 0) {
            try {
                price = getPriceFromUser(reader, writer);
            } catch (PriceFormatException e) {
                logger.warn(e.getMessage());
                writer.write(e.getMessage() + ". Try again. ");
            }
        }
        CoworkingType coworkingType = getCoworkingTypeFromUser(reader, writer);
        List<Facility> facilities = getFacilityFromUser(reader, writer);

        coworkingService.add(admin, price, coworkingType, facilities);

        writer.write("You just added a new Space:\n");
        writer.flush();
    }

    public void delete( BufferedReader reader, BufferedWriter writer, User user) throws IOException {
        List<CoworkingSpace> spaces = coworkingService.getAllByUser(user);
        boolean isDeleted = false;
        if (spaces.isEmpty()) {
            writer.write("Coworking List is empty\n");
            writer.flush();
        } else {
            while (!isDeleted) {
                spaces.forEach(System.out::println);
                writer.write("Type a coworking id you want to delete:\n");
                writer.flush();
                try {
                    String coworkingIdStr = reader.readLine();

                    while (!StringHandler.containsOnlyNumbers(coworkingIdStr)) {
                        writer.write("Wrong symbol. Try again.\nType a coworking id you want to delete:\n");
                        writer.flush();
                        coworkingIdStr = reader.readLine();
                    }

                    int coworkingId = Integer.parseInt(coworkingIdStr);
                    Optional<CoworkingSpace> possibleCoworking;

                    possibleCoworking = coworkingService.getById(coworkingId);
                    if (possibleCoworking.isPresent()) {
                        CoworkingSpace coworkingSpace = possibleCoworking.get();
                        coworkingService.delete(user, coworkingSpace);
                        writer.write("Coworking with id: " + coworkingId + " has been deleted\n");
                        writer.flush();
                        isDeleted = true;
                    }
                } catch (CoworkingNotFoundException e) {
                    logger.info(e.getMessage());
                    writer.write(e.getMessage() + "\n" + "Choose another Coworking \n");
                    writer.flush();
                } catch (ForbiddenActionException e) {
                    logger.error(e.getMessage());
                    writer.write(e.getMessage() + "\n");
                    writer.flush();
                }
            }
        }
    }

    public void getAllSpaces(BufferedWriter writer, User user) throws IOException {
        List<CoworkingSpace> spaces = coworkingService.getAllByUser(user);
        writer.write("Spaces list:\n");
        writer.flush();
        spaces.forEach(System.out::println);
    }

    public void load() {
        coworkingService.load();
    }

    public void save() {
        coworkingService.save();
    }

    private double getPriceFromUser(BufferedReader reader, BufferedWriter writer) throws
            IOException, PriceFormatException {
        writer.write("Enter the price in dollars per hour.\n");
        writer.flush();
        String price = reader.readLine();

        if (price == null || price.trim().isEmpty() || !price.matches("\\d+(\\.\\d+)?")) {
            throw new PriceFormatException(price);
        }

        return Double.parseDouble(price);
    }

    private CoworkingType getCoworkingTypeFromUser(BufferedReader reader, BufferedWriter writer) throws IOException {
        CoworkingType coworkingType = null;
        while (coworkingType == null) {
            try {
                writer.write(COWORKING_TYPE_MENU);
                writer.flush();
                int coworkingTypeIndex = Integer.parseInt(reader.readLine());
                if (coworkingTypeIndex < 0 || coworkingTypeIndex >= CoworkingType.values().length) {
                    writer.write("You put a wrong symbol. Try again\n");
                    writer.flush();
                    continue;
                }
                coworkingType = CoworkingType.values()[coworkingTypeIndex];
            } catch (NumberFormatException e) {
                writer.write("You entered wrong number. Try again\n");
                writer.flush();
            }
        }
        return coworkingType;
    }

    private List<Facility> getFacilityFromUser(BufferedReader reader, BufferedWriter writer) throws IOException {
        boolean areChosen = false;
        List<Facility> selectedFacilities = new ArrayList<>();
        while (!areChosen) {
            writer.write(FACILITY_MENU);
            writer.flush();

            String facilitiesIndexesOnOneLine = reader.readLine();
            if (facilitiesIndexesOnOneLine.matches("")) {
                areChosen = true;
            } else if (StringHandler.isFacilityStringFromUserValid(facilitiesIndexesOnOneLine)) {
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
                writer.write("You entered wrong number(s): " + facilitiesIndexesOnOneLine + ". Try again:\n");
                writer.flush();
            }
        }
        return selectedFacilities;
    }
}