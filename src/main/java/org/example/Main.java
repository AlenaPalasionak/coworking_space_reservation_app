package org.example;

import org.apache.logging.log4j.Logger;
import org.example.coworking.infrastructure.controller.AuthorizationController;
import org.example.coworking.infrastructure.controller.CoworkingController;
import org.example.coworking.infrastructure.controller.MenuController;
import org.example.coworking.infrastructure.controller.ReservationController;
import org.example.coworking.infrastructure.factory.AppFactory;
import org.example.coworking.infrastructure.logger.Log;
import org.example.coworking.model.Admin;
import org.example.coworking.model.Customer;
import org.example.coworking.model.Menu;
import org.example.coworking.model.User;

import java.io.*;
import java.util.Optional;

public class Main {
    private static final String WELCOME_MENU_KEY = "Welcome Menu";
    private static final String ADMIN_MENU_KEY = "Admin Menu1";
    private static final String CUSTOMER_MENU_KEY = "Customer Menu";
    private static final String NEXT_STEP_MENU_KEY = "Next Step Menu";
    public static final String EXIT = "0";
    public static final String ADMIN = "1";
    public static final String CUSTOMER = "2";
    public static final String LOG_OUT = "2";
    public static final String ADD_COWORKING_SPACE = "1";
    public static final String DELETE_COWORKING_SPACE = "2";
    public static final String GET_ALL_RESERVATIONS = "3";
    public static final String GET_AVAILABLE_COWORKING_SPACES = "1";
    public static final String ADD_RESERVATION = "2";
    public static final String GET_RESERVATIONS = "3";
    public static final String DELETE_RESERVATION = "4";

    private static final Logger logger = Log.getLogger(Main.class);
    public static User user;

    public static void main(String[] args) {
        AppFactory appFactory = new AppFactory();
        ReservationController reservationController = appFactory.createReservationController();
        AuthorizationController authorizationController = appFactory.createAuthorizationController();
        CoworkingController coworkingController = appFactory.createCoworkingController();
        MenuController menuController = appFactory.createMenuController();
        coworkingController.load();
        reservationController.load();
        menuController.getMenusFromStorage();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))
             ; BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out))) {
            while (true) {
                boolean logOut = false;
                String nextStep;
                Menu welcomeMenu = menuController.getMenuByName(WELCOME_MENU_KEY);
                menuController.showMenu(writer, welcomeMenu.getMenuName());
                String userRoleIdentifier = menuController.getUserChoice(reader, writer, welcomeMenu);
                if (userRoleIdentifier.equals(EXIT)) {
                    break;
                } else if (userRoleIdentifier.equals(ADMIN)) {
                    Optional<User> possibleAdmin = authorizationController.authenticate(reader, writer, Admin.class);
                    if (possibleAdmin.isPresent()) {
                        user = possibleAdmin.get();
                    }
                    while (!logOut) {
                        Menu adminMenu = menuController.getMenuByName(ADMIN_MENU_KEY);
                        menuController.showMenu(writer, adminMenu.getMenuName());
                        String adminOptionChoice = menuController.getUserChoice(reader, writer, adminMenu);
                        switch (adminOptionChoice) {
                            case ADD_COWORKING_SPACE:
                                coworkingController.add(reader, writer, user);
                                break;
                            case DELETE_COWORKING_SPACE:
                                coworkingController.delete(reader, writer, user);
                                break;
                            case GET_ALL_RESERVATIONS:
                                reservationController.getAllReservations(writer, user);
                                break;
                        }
                        Menu nextStepMenu = menuController.getMenuByName(NEXT_STEP_MENU_KEY);
                        menuController.showMenu(writer, nextStepMenu.getMenuName());
                        nextStep = menuController.getUserChoice(reader, writer, nextStepMenu);
                        if (nextStep.equals(LOG_OUT)) {
                            logOut = true;
                        } else if (nextStep.equals(EXIT)) {
                            coworkingController.save();
                            reservationController.save();
                            System.exit(0);
                        }
                    }
                } else if (userRoleIdentifier.equals(CUSTOMER)) {
                    Optional<User> possibleCustomer = authorizationController.authenticate
                            (reader, writer, Customer.class);
                    if (possibleCustomer.isPresent()) {
                        user = possibleCustomer.get();
                    }
                    while (!logOut) {
                        Menu customerMenu = menuController.getMenuByName(CUSTOMER_MENU_KEY);
                        menuController.showMenu(writer, customerMenu.getMenuName());
                        ;
                        String customerOptionChoice = menuController.getUserChoice
                                (reader, writer, customerMenu);
                        switch (customerOptionChoice) {
                            case GET_AVAILABLE_COWORKING_SPACES:
                                coworkingController.getAllSpaces(writer, user);
                                break;
                            case ADD_RESERVATION:
                                reservationController.add(reader, writer, user);
                                break;
                            case GET_RESERVATIONS:
                                reservationController.getAllReservations(writer, user);
                                break;
                            case DELETE_RESERVATION:
                                reservationController.delete(reader, writer, user);
                                break;
                        }
                        Menu nextStepMenu = menuController.getMenuByName(NEXT_STEP_MENU_KEY);
                        menuController.showMenu(writer, nextStepMenu.getMenuName());
                        nextStep = menuController.getUserChoice(reader, writer, nextStepMenu);
                        if (nextStep.equals(LOG_OUT)) {
                            logOut = true;
                        } else if (nextStep.equals(EXIT)) {
                            coworkingController.save();
                            reservationController.save();
                            System.exit(0);
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Error while reading or writing from console. " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}