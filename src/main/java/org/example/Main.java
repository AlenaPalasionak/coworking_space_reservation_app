package org.example;

import org.example.coworking.infrastructure.factory.AppFactory;
import org.example.coworking.infrastructure.controller.AuthorizationController;
import org.example.coworking.infrastructure.controller.CoworkingController;
import org.example.coworking.infrastructure.controller.ReservationController;
import org.example.coworking.infrastructure.menu.Menu;
import org.example.coworking.infrastructure.menu.MenuImpl;
import org.example.coworking.model.Admin;
import org.example.coworking.model.Customer;
import org.example.coworking.model.User;

import java.io.*;
import java.util.Optional;

public class Main {
    static User user;
    static ReservationController reservationController = AppFactory.createReservationController();
    static AuthorizationController authorizationController = AppFactory.createAuthorizationController();
    static CoworkingController coworkingController = AppFactory.createCoworkingController();

    public static final String WELCOME_MENU = """
            Welcome to the Coworking Space Reservation!
            If you are an *admin* press 1
            If you are a *customer* press 2
            If you want to exit press 0
            """;

    public static final String ADMIN_MENU = """
            Press 1 to add a new coworking space.
            Press 2 to remove a coworking space.
            Press 3 to view all reservations.
            """;

    public static final String CUSTOMER_MENU = """
            Press 1 to browse available spaces.
            Press 2 to make a reservation.
            Press 3 to view your reservations.
            Press 4 to cancel a reservation.
            """;

    public static final String NEXT_STEP_MENU = """
            Log out?
            No - press 1
            Yes - press 2
            Cancel the program - press 0
            """;

    public static final String EXIT = "0";
    public static final String ADMIN = "1";
    public static final String CUSTOMER = "2";
    public static final String LOG_OUT = "2";
    public static final String[] MAIN_MENU_POSSIBLE_CHOICES = {"1", "2", "0"};
    public static final String[] ADMIN_OPTION_POSSIBLE_CHOICES = {"1", "2", "3"};
    public static final String[] NEXT_STEP_POSSIBLE_CHOICES = {"1", "2", "0"};
    public static final String[] CUSTOMER_OPTION_POSSIBLE_CHOICES = {"1", "2", "3", "4"};

    public static void main(String[] args) {
        coworkingController.getCoworkingPlacesFromJson();
        reservationController.getReservationsFromJson();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out))) {
            while (true) {
                boolean logOut = false;
                String nextStep;
                Menu welcomeMenu = new MenuImpl(WELCOME_MENU);
                welcomeMenu.showMenu(reader, writer);

                String userRoleIdentifier = welcomeMenu.getUserChoice(reader, writer, MAIN_MENU_POSSIBLE_CHOICES);
                if (userRoleIdentifier.equals(EXIT)) {
                    break;
                } else if (userRoleIdentifier.equals(ADMIN)) {
                    Optional<User> possibleAdmin = authorizationController.authenticate(reader, writer, Admin.class);
                    if (possibleAdmin.isPresent()) {
                        user = possibleAdmin.get();
                    }
                    while (!logOut) {
                        Menu adminMenu = new MenuImpl(ADMIN_MENU);
                        adminMenu.showMenu(reader, writer);
                        String adminOptionChoice = adminMenu.getUserChoice(reader, writer, ADMIN_OPTION_POSSIBLE_CHOICES);
                        switch (adminOptionChoice) {
                            case "1":
                                coworkingController.add(reader, writer);
                                break;
                            case "2":
                                coworkingController.delete(user, reader, writer);
                                break;
                            case "3":
                                reservationController.getAllReservations(writer, user);
                                break;
                        }
                        Menu nextStepMenu = new MenuImpl(NEXT_STEP_MENU);
                        nextStepMenu.showMenu(reader, writer);
                        nextStep = nextStepMenu.getUserChoice(reader, writer, NEXT_STEP_POSSIBLE_CHOICES);
                        if (nextStep.equals(LOG_OUT)) {
                            logOut = true;
                        } else if (nextStep.equals(EXIT)) {
                            coworkingController.saveToJSON();
                            reservationController.saveToJSON();
                            System.exit(0);
                        }
                    }
                } else if (userRoleIdentifier.equals(CUSTOMER)) {
                    Optional<User> possibleCustomer = authorizationController.authenticate(reader, writer, Customer.class);
                    if (possibleCustomer.isPresent()) {
                        user = possibleCustomer.get();
                    }
                    while (!logOut) {
                        Menu customerMenu = new MenuImpl(CUSTOMER_MENU);
                        customerMenu.showMenu(reader, writer);
                        String customerOptionChoice = customerMenu.getUserChoice(reader, writer, CUSTOMER_OPTION_POSSIBLE_CHOICES);
                        switch (customerOptionChoice) {
                            case "1":
                                coworkingController.getAllSpaces(writer);
                                break;
                            case "2":
                                reservationController.add(reader, writer, user);
                                break;
                            case "3":
                                reservationController.getAllReservations(writer, user);
                                break;
                            case "4":
                                reservationController.delete(reader, writer, user);
                                break;
                        }
                        Menu nextStepMenu = new MenuImpl(NEXT_STEP_MENU);
                        nextStepMenu.showMenu(reader, writer);
                        nextStep = nextStepMenu.getUserChoice(reader, writer, NEXT_STEP_POSSIBLE_CHOICES);
                        if (nextStep.equals(LOG_OUT)) {
                            logOut = true;
                        } else if (nextStep.equals(EXIT)) {
                            coworkingController.saveToJSON();
                            reservationController.saveToJSON();
                            System.exit(0);
                        }
                    }
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
