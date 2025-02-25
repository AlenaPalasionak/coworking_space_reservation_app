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

    default boolean shouldExit(BufferedReader reader, BufferedWriter writer) throws IOException {
        writer.write("""
                                            
                Cansel the program?
                No - press 0
                Yes - press 1
                                                        
                """);
        writer.flush();
        String exitNotification2 = reader.readLine();
        return exitNotification2.equals("1");
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