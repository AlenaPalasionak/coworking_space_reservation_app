package org.example;

import org.example.coworking.model.Admin;
import org.example.coworking.model.Customer;
import org.example.coworking.util.*;

import java.io.*;
import java.util.Optional;

public class Main {
    static Admin admin = new Admin();
    static Customer customer = new Customer();
    ;
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
                String userRoleIdentifier = BaseHelperImpl.getUserRoleIdentifier(reader, writer);
                if (userRoleIdentifier.equals(EXIT)) {
                    break;
                } else if (userRoleIdentifier.equals(ADMIN)) {
                    Optional<Admin> adminOptional = BaseHelperImpl.logIn(reader, writer, Admin.class);
                    Admin admin = adminOptional.get();
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
                        nextStep = BaseHelperImpl.chooseNextStep(reader, writer);
                        if (nextStep.equals(EXIT)) {
                            logOut = true;
                        }
                    }
                } else if (userRoleIdentifier.equals(CUSTOMER)) {
                    Optional<Customer> customerOptional = BaseHelperImpl.logIn(reader, writer, Customer.class);
                    Customer customer = customerOptional.get();
                    while (!logOut) {
                        String customerOptionChoice = customerHelper.showMenu(reader, writer);
                        switch (customerOptionChoice) {
                            case GET_AVAILABLE_COWORKING_SPACES:
                                BaseHelperImpl.getAllCoworkingPlaces(writer, customer);
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
                        nextStep = BaseHelperImpl.chooseNextStep(reader, writer);
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
