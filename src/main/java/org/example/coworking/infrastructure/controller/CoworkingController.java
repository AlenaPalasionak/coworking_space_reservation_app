package org.example.coworking.infrastructure.controller;

import org.example.coworking.infrastructure.util.StringHandler;
import org.example.coworking.model.CoworkingSpace;
import org.example.coworking.model.CoworkingType;
import org.example.coworking.model.Facility;
import org.example.coworking.model.User;
import org.example.coworking.service.CoworkingService;
import org.example.coworking.service.CoworkingServiceImpl;
import org.example.coworking.service.ReservationService;
import org.example.coworking.service.ReservationServiceImpl;
import org.example.coworking.service.exception.CoworkingNotFoundException;
import org.example.coworking.service.exception.ForbiddenActionException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class CoworkingController {

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

    public CoworkingController() {
        this.coworkingService = new CoworkingServiceImpl();
        this.reservationService = new ReservationServiceImpl();
    }

    public void add(BufferedReader reader, BufferedWriter writer) throws IOException {
        double price = getPriceFromUser(reader, writer);
        CoworkingType coworkingType = getCoworkingTypeFromUser(reader, writer);
        Facility facility = getFacilityFromUser(reader, writer);

        coworkingService.add(price, coworkingType, facility);

        writer.write("You just added a new Space:\n");
        writer.flush();
    }

    public void delete(User user, BufferedReader reader, BufferedWriter writer) throws IOException {
            writer.write("Type a coworking id you want to delete:\n" + coworkingService.getAllSpaces());
            writer.flush();
            writer.write("Coworking List is empty\n");
            writer.flush();
        try {
            int coworkingId = Integer.parseInt(reader.readLine());
            coworkingService.delete(user, coworkingId);
            writer.write("Coworking with id: " + coworkingId + " has been deleted\n");
            writer.flush();
        } catch (ForbiddenActionException e) {
            writer.write("You do not have permission to delete this coworking space.\n");
            writer.flush();
        } catch (CoworkingNotFoundException e) {
            writer.write("There is no space with id." + user.getId() + "\n");
            writer.flush();
        }
    }

    public void getAllSpaces(BufferedWriter writer) throws IOException {
        List<CoworkingSpace> spaces = coworkingService.getAllSpaces();
        writer.write("Spaces list:\n" + spaces + "\n");
    }

    private double getPriceFromUser(BufferedReader reader, BufferedWriter writer) throws IOException {
        writer.write("Enter the price in dollars per hour.\n");
        writer.flush();
        return Double.parseDouble(reader.readLine());
    }

    private CoworkingType getCoworkingTypeFromUser(BufferedReader reader, BufferedWriter writer) throws IOException {
        writer.write(COWORKING_TYPE_MENU);
        writer.flush();

        CoworkingType coworkingType = null;
        while (coworkingType == null) {
            try {
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

    private Facility getFacilityFromUser(BufferedReader reader, BufferedWriter writer) throws IOException {
        writer.write(FACILITY_MENU);
        writer.flush();

        Facility.FacilityBuilder builder = new Facility.FacilityBuilder();
        String facilitiesIndexesOnOneLine = reader.readLine();

        if (StringHandler.containsDigits(facilitiesIndexesOnOneLine)) {
            String[] facilitiesIndexes = facilitiesIndexesOnOneLine.split(",");
            for (String facilitiesIndex : facilitiesIndexes) {
                int facilityIndex = Integer.parseInt(facilitiesIndex.trim());
                builder.addFeature(Facility.Feature.values()[facilityIndex]);
            }
        }
        return builder.build();
    }
}
