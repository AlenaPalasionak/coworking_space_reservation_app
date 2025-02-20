package org.example;

import org.example.coworking.model.Admin;
import org.example.coworking.model.Coworking;
import org.example.coworking.model.Customer;
import org.example.coworking.model.Reservation;
import org.example.coworking.util.AdminControllerHelper;
import org.example.coworking.util.CustomerControllerHelper;
import org.example.coworking.util.PasswordValidator;

import java.io.*;
import java.util.List;

import static org.example.coworking.util.BaseHelper.shouldExit;

public class Main {
    static Admin admin;
    static Customer customer;

    public static void main(String[] args) {
        PasswordValidator.loadLoginData();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out))) {

            while (true) {
                while (true) {
                    writer.write("""
                                                    
                            Welcome to the Coworking Space Reservation!
                            If you are an *admin* press 1
                            If you are a *customer* press 2
                            If you want to exit press 0
                                                
                            """);
                    writer.flush();

                    String userRoleIdentifier = reader.readLine();
                    if (userRoleIdentifier.equals("0")) {
                        break;
                    } else if (userRoleIdentifier.equals("1")) {
                        admin = AdminControllerHelper.adminLogIn(reader, writer);

                        String adminOptionChoice = AdminControllerHelper.showAdminMenu(reader, writer);

                        switch (adminOptionChoice) {
                            case "1":
                                AdminControllerHelper.addNewCoworking(reader, writer);
                                if (shouldExit(reader, writer)) {
                                    return;
                                }
                                break;
                            case "2":
                                AdminControllerHelper.removeCoworkingSpace(reader, writer);
                                if (shouldExit(reader, writer)) {
                                    return;
                                }
                                break;
                            case "3":
                                List<Coworking> coworkings = AdminControllerHelper.getAllCoworkingSpaces(writer);
                                writer.write("""
                                        Your coworking Spaces"""
                                        + coworkings);
                                writer.flush();
                                if (shouldExit(reader, writer)) {
                                    return;
                                }
                                break;
                        }
                    } else if (userRoleIdentifier.equals("2")) {
                        customer = CustomerControllerHelper.customerLogIn(reader, writer);
                        while (true) {
                            String customerOptionChoice = CustomerControllerHelper.showCustomerMenu(reader, writer);
                            switch (customerOptionChoice) {
                                case "1":
                                    List<Coworking> coworkings = CustomerControllerHelper.getAllCoworkingSpaces(writer);
                                    writer.write("""
                                            Available coworking Spaces"""
                                            + coworkings);
                                    writer.flush();
                                    if (shouldExit(reader, writer)) {
                                        return;
                                    }
                                    break;
                                case "2":
                                    CustomerControllerHelper.addReservation(reader, writer, customer);

                                    if (shouldExit(reader, writer)) {
                                        return;
                                    }
                                    break;
                                case "3":
                                    List<Reservation> reservations = CustomerControllerHelper.getCustomerReservations(writer, customer);
                                    writer.write("""
                                            Your reservation(s)
                                            """ + reservations);
                                    writer.flush();
                                    if (shouldExit(reader, writer)) {
                                        return;
                                    }
                                    break;
                                case "4":
                                    CustomerControllerHelper.cancelReservation(reader, writer, customer);
                                    if (shouldExit(reader, writer)) {
                                        return;
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
