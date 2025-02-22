package org.example.coworking.util;

import org.example.coworking.model.Coworking;
import org.example.coworking.model.Reservation;
import org.example.coworking.model.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import static org.example.coworking.util.CustomerHelper.adminController;

public interface BaseHelper {
    String WELCOME_MENU = """
                                    
            Welcome to the Coworking Space Reservation!
            If you are an *admin* press 1
            If you are a *customer* press 2
            If you want to exit press 0
                                
            """;

    static String getUserRoleIdentifier(BufferedReader reader, BufferedWriter writer) throws IOException {
        String userRoleIdentifier;
        while (true) {
            writer.write(WELCOME_MENU);
            writer.flush();
            userRoleIdentifier = reader.readLine();
            if (!(userRoleIdentifier.equals("0") || userRoleIdentifier.equals("1") || userRoleIdentifier.equals("2"))) {
                writer.write("You entered the wrong symbol, try again:\n");
            } else {
                return userRoleIdentifier;
            }
        }
    }

    static String chooseNextStep(BufferedReader reader, BufferedWriter writer) throws IOException {
        while (true) {
            writer.write("""
                                    
                    Menu - press 1
                    Log out - press 0
                                
                    """);
            writer.flush();
            String exitNotification = reader.readLine();
            if (!(exitNotification.equals("0") || exitNotification.equals("1"))) {
                writer.write("You entered the wrong symbol, try again:\n");
            } else return exitNotification;
        }
    }

    default List<Coworking> getAllCoworkingPlaces(BufferedWriter writer, User customer) throws IOException {
        List<Coworking> coworkingSpaces = adminController.getAllCoworkingSpaces();
        if (coworkingSpaces.isEmpty()) {
            writer.write("Coworking List is empty\n");
            writer.flush();
        }
        writer.write(customer.getName() + ", here is coworking List:\n" + coworkingSpaces);
        writer.flush();
        return coworkingSpaces;
    }

    String showMenu(BufferedReader reader, BufferedWriter writer) throws IOException;

    User logIn(BufferedReader reader, BufferedWriter writer) throws IOException;

    void add(BufferedReader reader, BufferedWriter writer, User user) throws IOException;

    List<Reservation> getAllReservations(BufferedWriter writer, User customer) throws IOException;

    void delete(BufferedReader reader, BufferedWriter writer, User user) throws IOException;
}