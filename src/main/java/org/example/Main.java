package org.example;

import org.example.coworking.model.User;
import org.example.coworking.util.AdminHelper;
import org.example.coworking.util.BaseHelper;
import org.example.coworking.util.CustomerHelper;
import org.example.coworking.util.PasswordValidator;

import java.io.*;

public class Main {
    static User admin;
    static User customer;
    static BaseHelper adminHelper = new AdminHelper();
    static BaseHelper customerHelper = new CustomerHelper();
    public static final String EXIT = "0";
    public static final String ADMIN = "1";
    public static final String CUSTOMER = "2";
    public static final String ADD_COWORKING_SPACE = "1";
    public static final String DELETE_COWORKING_SPACE = "2";
    public static final String GET_ALL_RESERVATIONS = "3";
    public static final String GET_AVAILABLE_COWORKING_SPACES = "1";
    public static final String ADD_RESERVATION = "2";
    public static final String GET_RESERVATIONS = "3";
    public static final String DELETE_RESERVATION = "4";

    public static void main(String[] args) {
        PasswordValidator.loadLoginData();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out))) {

            while (true) {
                boolean logOut = false;
                String nextStep;
                String userRoleIdentifier = BaseHelper.getUserRoleIdentifier(reader, writer);

                if (userRoleIdentifier.equals(EXIT)) {
                    break;
                } else if (userRoleIdentifier.equals(ADMIN)) {
                    admin = adminHelper.logIn(reader, writer);

                    while (!logOut) {
                        String adminOptionChoice = adminHelper.showMenu(reader, writer);
                        switch (adminOptionChoice) {
                            case ADD_COWORKING_SPACE:
                                adminHelper.add(reader, writer, admin);
                                break;
                            case DELETE_COWORKING_SPACE:
                                adminHelper.delete(reader, writer, admin);
                                break;
                            case GET_ALL_RESERVATIONS:
                                adminHelper.getAllReservations(writer, admin);
                                break;
                        }
                        nextStep = BaseHelper.chooseNextStep(reader, writer);
                        if (nextStep.equals(EXIT)) {
                            logOut = true;
                        }
                    }
                } else if (userRoleIdentifier.equals(CUSTOMER)) {
                    customer = customerHelper.logIn(reader, writer);

                    while (!logOut) { // Используем логический выход из цикла
                        String customerOptionChoice = customerHelper.showMenu(reader, writer);
                        switch (customerOptionChoice) {
                            case GET_AVAILABLE_COWORKING_SPACES:
                                customerHelper.getAllCoworkingPlaces(writer, customer);
                                break;
                            case ADD_RESERVATION:
                                customerHelper.add(reader, writer, customer);
                                break;
                            case GET_RESERVATIONS:
                                customerHelper.getAllReservations(writer, customer);
                                break;
                            case DELETE_RESERVATION:
                                customerHelper.delete(reader, writer, customer);
                                break;
                        }
                        nextStep = BaseHelper.chooseNextStep(reader, writer);
                        if (nextStep.equals(EXIT)) {
                            logOut = true;
                        }
                    }
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
