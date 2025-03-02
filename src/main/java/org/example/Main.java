package org.example;

import org.example.coworking.infrastructure.controller.*;
import org.example.coworking.infrastructure.menu.Menu;
import org.example.coworking.infrastructure.menu.MenuImpl;
import org.example.coworking.model.Admin;
import org.example.coworking.model.Customer;
import org.example.coworking.model.User;

import java.io.*;
import java.util.Optional;

public class Main {
    static User user;
    static ReservationController reservationController = new ReservationController();
    static AuthorizationController authorizationController = new AuthorizationController();
    static CoworkingController coworkingController = new CoworkingController();
    public static final String WELCOME_MENU = """
                                    
            Welcome to the Coworking Space Reservation!
            If you are an *admin* press 1
            If you are a *customer* press 2
            If you want to exit press 0
                                
            """;
    public static final String ADMIN_MENU = """ 
            Press 1 to add a new coworkingSpace space.
            Press 2 to remove a coworkingSpace space.
            Press 3 to view all reservations.
            """;
    public static final String CUSTOMER_MENU = """ 
            Press 1 to browse available spaces.
            Press 2 to Make a reservation.
            Press 3 to view your reservations.
            Press 4 to cancel a reservation.
            """;

    public static final String NEXT_STEP_MENU = """
            Cansel the program?
            No - press 1
            Yes - press 0
            """;

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

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out))) {
            while (true) {
                boolean logOut = false;
                String nextStep;
                Menu welcomeMenu = new MenuImpl(WELCOME_MENU);
                welcomeMenu.showMenu(reader, writer);

                String userRoleIdentifier = welcomeMenu.getUserChoice(reader);
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
                        String adminOptionChoice = adminMenu.getUserChoice(reader);
                        switch (adminOptionChoice) {
                            case ADD_COWORKING_SPACE:
                                coworkingController.add(reader, writer);
                                break;
                            case DELETE_COWORKING_SPACE:
                                coworkingController.delete(user, reader, writer);
                                break;
                            case GET_ALL_RESERVATIONS:
                                reservationController.getAllReservations(writer, user);
                                break;
                        }
                        Menu nextStepMenu = new MenuImpl(NEXT_STEP_MENU);
                        nextStepMenu.showMenu(reader, writer);
                        nextStep = nextStepMenu.getUserChoice(reader);
                        if (nextStep.equals(EXIT)) {
                            logOut = true;
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
                        String customerOptionChoice = customerMenu.getUserChoice(reader);
                        switch (customerOptionChoice) {
                            case GET_AVAILABLE_COWORKING_SPACES:
                                coworkingController.getAllSpaces(writer);
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
                        Menu nextStepMenu = new MenuImpl(NEXT_STEP_MENU);
                        nextStepMenu.showMenu(reader, writer);
                        nextStep = nextStepMenu.getUserChoice(reader);
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
