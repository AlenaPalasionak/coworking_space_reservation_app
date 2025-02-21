package org.example.coworking.util;

import org.example.coworking.controller.AdminController;
import org.example.coworking.dao.IdGenerator;
import org.example.coworking.model.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class AdminHelper implements BaseHelper {

    private static final AdminController adminController = new AdminController();
    public static final String ADMIN_MENU = """ 
            Press 1 to add a new coworking space.
            Press 2 to remove a coworking space.
            Press 3 to view all reservations.
            """;
    public static final String COWORKING_TYPE_MENU = """
            Choose the coworking type (press only one of the numbers):
            Open space coworking - 0
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

    @Override
    public String showMenu(BufferedReader reader, BufferedWriter writer) throws IOException {
        writer.write(ADMIN_MENU);
        writer.flush();
        return reader.readLine();
    }

    @Override
    public User logIn(BufferedReader reader, BufferedWriter writer) throws IOException {
        Admin admin = null;
        boolean isLoggedIn = false;
        while (!isLoggedIn) {
            writer.write("Enter your admin name, please.\n");
            writer.flush();
            String adminName = reader.readLine().trim();
            writer.write(adminName + ", Enter your admin password, please.\n");
            writer.flush();
            String adminPassword = reader.readLine().trim();
            if (PasswordValidator.isAdminLoginDataValid(adminName, adminPassword)) {
                isLoggedIn = true;
                int adminId = IdGenerator.generateUserId();
                admin = new Admin(adminId, adminName, adminPassword);
                writer.write("You have successfully logged in.\n");
                writer.flush();
            } else {
                writer.write("Your login data are wrong. To try again\n");
                writer.flush();
            }
        }
        return admin;
    }

    @Override
    public void add(BufferedReader reader, BufferedWriter writer, User admin) throws IOException {
        Coworking space;
        int id = IdGenerator.generateCoworkingId();
        writer.write("Enter the price in dollars per hour.\n");
        writer.flush();

        double price = Double.parseDouble(reader.readLine());
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
                System.out.println("You entered wrong number" + e.getMessage());
            }
        }
        Facility.FacilityBuilder builder = new Facility.FacilityBuilder();
        writer.write(FACILITY_MENU);
        writer.flush();

        Facility facility = null;
        String facilitiesIndexesOnOneLine = reader.readLine();
        if (StringHandler.containsDigits(facilitiesIndexesOnOneLine)) {
            String[] facilitiesIndexes = facilitiesIndexesOnOneLine.split(",");
            for (String facilitiesIndex : facilitiesIndexes) {
                int facilityIndex = Integer.parseInt(facilitiesIndex.trim());
                builder.addFeature(Facility.Feature.values()[facilityIndex]);
            }
            facility = builder.build();
        }
        space = new Coworking(id, price, coworkingType, facility);
        adminController.addCoworkingSpace(space);

        writer.write(admin.getName() + ", you just added a new Space:\n" +
                adminController.getSpaceById(id));
    }

    @Override
    public List<Reservation> getAllReservations(BufferedWriter writer, User customer) throws IOException {
        List<Reservation> allReservations = adminController.getAllReservations();
        writer.write("All Reservations:\n" + allReservations);
        return allReservations;
    }

    @Override
    public void delete(BufferedReader reader, BufferedWriter writer, User admin) throws IOException {
        List<Coworking> spaces = adminController.getAllCoworkingSpaces();
        if (spaces.isEmpty()) {
            writer.write("There are no available spaces.\n");
            writer.flush();
        } else {
            writer.write("Available spaces:\n" + adminController.getAllCoworkingSpaces());

            writer.flush();
            writer.write("Type the id of the space you want to delete\n");
            writer.flush();

            int coworkingSpaceId = Integer.parseInt(reader.readLine().trim());
            adminController.removeCoworkingSpace(coworkingSpaceId);
            writer.write(admin.getName() + ", you just removed a Space with id: " + coworkingSpaceId);
        }
    }
}
