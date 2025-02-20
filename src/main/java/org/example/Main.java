package org.example;

import org.example.coworking.model.User;
import org.example.coworking.util.AdminHelper;
import org.example.coworking.util.BaseHelper;
import org.example.coworking.util.CustomerHelper;
import org.example.coworking.util.PasswordValidator;

import java.io.*;

import static org.example.coworking.util.Constant.*;

public class Main {
    static User admin;
    static User customer;
    static BaseHelper adminHelper = new AdminHelper();
    static BaseHelper customerHelper = new CustomerHelper();

    public static void main(String[] args) {
        PasswordValidator.loadLoginData();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out))) {

            while (true) {
                while (true) {
                    writer.write(WELCOME_MENU);
                    writer.flush();

                    String userRoleIdentifier = reader.readLine();
                    if (userRoleIdentifier.equals(EXIT)) {
                        break;
                    } else if (userRoleIdentifier.equals(ADMIN)) {
                        admin = adminHelper.logIn(reader, writer);
                        String adminOptionChoice = adminHelper.showMenu(reader, writer);

                        switch (adminOptionChoice) {
                            case ADD_COWORKING_SPACE:
                                adminHelper.add(reader, writer, admin);
                                if (adminHelper.shouldExit(reader, writer)) {
                                    return;
                                }
                                break;
                            case DELETE_COWORKING_SPACE:
                                adminHelper.delete(reader, writer, admin);
                                if (adminHelper.shouldExit(reader, writer)) {
                                    return;
                                }
                                break;
                            case GET_ALL_RESERVATIONS:
                                adminHelper.getAllReservations(writer, admin);
                                if (adminHelper.shouldExit(reader, writer)) {
                                    return;
                                }
                                break;
                        }
                    } else if (userRoleIdentifier.equals(CUSTOMER)) {
                        customer = customerHelper.logIn(reader, writer);
                        while (true) {
                            String customerOptionChoice = customerHelper.showMenu(reader, writer);
                            switch (customerOptionChoice) {
                                case GET_AVAILABLE_COWORKING_SPACES:
                                    customerHelper.getAllCoworkingPlaces(writer, customer);
                                    if (customerHelper.shouldExit(reader, writer)) {
                                        return;
                                    }
                                    break;
                                case ADD_RESERVATION:
                                    customerHelper.add(reader, writer, customer);
                                    if (customerHelper.shouldExit(reader, writer)) {
                                        return;
                                    }
                                    break;
                                case GET_RESERVATIONS:
                                    customerHelper.getAllReservations(writer, customer);
                                    if (customerHelper.shouldExit(reader, writer)) {
                                        return;
                                    }
                                    break;
                                case DELETE_RESERVATION:
                                    customerHelper.delete(reader, writer, customer);
                                    if (customerHelper.shouldExit(reader, writer)) {
                                        return;
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
