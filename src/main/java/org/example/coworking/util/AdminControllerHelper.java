package org.example.coworking.util;

import org.example.coworking.controller.AdminController;
import org.example.coworking.model.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class AdminControllerHelper {

    private static final AdminController adminController = new AdminController();


    public static String showAdminMenu(BufferedReader reader, BufferedWriter writer) throws IOException {
        writer.write(""" 
                                
                Press 1 to add a new coworking space.
                Press 2 to remove a coworking space.
                Press 3 to view all coworking places.
                                
                """);
        writer.flush();
        return reader.readLine();
    }

    public static Admin adminLogIn(BufferedReader reader, BufferedWriter writer) throws IOException {
        Admin admin = null;
        boolean isLoggedIn = false;
        while (!isLoggedIn) {
            writer.write("""
                                        
                    Enter your admin name, please.
                                        
                    """);
            writer.flush();
            String adminName = reader.readLine().trim();
            writer.write(adminName + ", " + """
                    Enter your admin password, please.
                                        
                    """);
            writer.flush();
            String adminPassword = reader.readLine().trim();
            if (PasswordValidator.isAdminLoginDataValid(adminName, adminPassword)) {
                isLoggedIn = true;
                int adminId = IdGenerator.generateUserId();
                admin = new Admin(adminId, adminName, adminPassword);
                writer.write("""
                                                
                        You have successfully logged in.
                                                
                        """);
                writer.flush();
            } else {
                writer.write("""
                                                
                        Your login data are wrong. To try again
                                                
                        """);
                writer.flush();
            }
        }
        return admin;
    }

    public static void addNewCoworking(BufferedReader reader, BufferedWriter writer) throws IOException {
        Coworking space;
        int id = IdGenerator.generateCoworkingId();
        writer.write("""
                                                        
                Enter the price in dollars per hour.
                                
                """);
        writer.flush();

        double price = Double.parseDouble(reader.readLine());
        boolean isAvailable = true;
        writer.write("""
                                                        
                Choose the coworking type (press only one of the numbers):
                Open space coworking - 0
                Private Office - 1
                Coworking+Co-living - 2
                                
                """);
        writer.flush();
        CoworkingType coworkingType = null;

        while (coworkingType == null) {
            try {
                int coworkingTypeIndex = Integer.parseInt(reader.readLine());
                if (coworkingTypeIndex < 0 || coworkingTypeIndex >= CoworkingType.values().length) {
                    writer.write("""
                            You put a wrong symbol. Try again
                            """);
                    writer.flush();
                    continue;
                }
                coworkingType = CoworkingType.values()[coworkingTypeIndex];
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите корректное число." + e.getMessage());
            }
        }
            Facility.FacilityBuilder builder = new Facility.FacilityBuilder();
            writer.write("""
                                                            
                    Choose the facilities. Write numbers comma-separated on one line:
                    NO Facilities - just press Enter,
                    PARKING - 0,
                    WIFI - 1,
                    KITCHEN - 2,
                    PRINTER - 3,
                    CONDITIONING - 4
                                    
                    """);
            writer.flush();

            String facilitiesIndexesOnOneLine = reader.readLine();
            if (!StringHandler.containsDigits(facilitiesIndexesOnOneLine)) {
                space = new Coworking.CoworkingSpaceBuilder(id, price, isAvailable, coworkingType)
                        .build();
                adminController.addCoworkingSpace(space);
            } else {
                String[] facilitiesIndexes = facilitiesIndexesOnOneLine.split(",");
                for (int i = 0; i < facilitiesIndexes.length; i++) {
                    int facilityIndex = Integer.parseInt(facilitiesIndexes[i].trim());
                    builder.addFeature(Facility.Feature.values()[facilityIndex]);
                }
                Facility facility = builder.build();

                space = new Coworking.CoworkingSpaceBuilder(id, price, isAvailable, coworkingType)
                        .setFacility(facility)
                        .build();

                adminController.addCoworkingSpace(space);
            }
            writer.write("""

                    You just added a new Space:
                    """ + adminController.getSpaceById(id) + """
                                    
                    """);
        }

        public static void removeCoworkingSpace (BufferedReader reader, BufferedWriter writer) throws IOException {
            List<Coworking> spaces = adminController.getAllCoworkingSpaces();
            if (spaces.isEmpty()) {
                writer.write("""
                        There are no available spaces:
                        """);
                writer.flush();
            } else {
                writer.write("""
                        Available spaces:
                        """ + adminController.getAllCoworkingSpaces());

                writer.flush();
                writer.write("""
                                        
                        Type the id of the space you want to delete
                                        
                        """);
                writer.flush();

                int spaceId = Integer.parseInt(reader.readLine().trim());
                adminController.removeCoworkingSpace(spaceId);
                writer.write("You just removed a Space with id: " + spaceId);
            }
        }

        public static List<Coworking> getAllCoworkingSpaces (BufferedWriter writer) throws
        IOException {
            List<Coworking> coworkingSpaces = adminController.getAllCoworkingSpaces();
            if (coworkingSpaces.isEmpty()) {
                writer.write("""
                        You haven't made any reservations yet
                        """);
                writer.flush();
            }

            return coworkingSpaces;
        }
    }
