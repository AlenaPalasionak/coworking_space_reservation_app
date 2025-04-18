package org.example.coworking.controller;

import org.example.coworking.model.Admin;
import org.example.coworking.model.Customer;
import org.example.coworking.model.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.example.coworking.logger.Log.TECHNICAL_LOGGER;

/**
 * Handles the main application flow, delegate actions to appropriate controllers
 * for admin and customer roles.
 */
@Component
public class FlowController {
    private final AuthorizationController authorizationController;
    private final CoworkingController coworkingController;
    private final ReservationController reservationController;
    private final MenuController menuController;
    private static final String ADD_COWORKING_SPACE = "1";
    private static final String DELETE_COWORKING_SPACE = "2";
    private static final String GET_ALL_RESERVATIONS = "3";
    private static final String GET_AVAILABLE_COWORKING_SPACES = "1";
    private static final String ADD_RESERVATION = "2";
    private static final String GET_RESERVATIONS = "3";
    private static final String DELETE_RESERVATION = "4";
    private static final String ADMIN_MENU_KEY = "Admin Menu";
    private static final String CUSTOMER_MENU_KEY = "Customer Menu";
    private static final String NEXT_STEP_MENU_KEY = "Next Step Menu";
    private static final String WELCOME_MENU_KEY = "Welcome Menu";
    private static final String ADMIN = "1";
    private static final String CUSTOMER = "2";
    private static final String EXIT = "0";
    private static final String LOG_OUT = "2";

    @Autowired
    public FlowController(AuthorizationController authorizationController, CoworkingController coworkingController,
                          ReservationController reservationController, MenuController menuController) {
        this.authorizationController = authorizationController;
        this.coworkingController = coworkingController;
        this.reservationController = reservationController;
        this.menuController = menuController;
    }

    /**
     * Starts the main application flow by displaying the welcome menu and
     * routing the user to the appropriate flow (admin or customer) based on input.
     * Handles user input through the console and ensures graceful termination.
     */
    public void startAppFlow() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            boolean running = true;
            while (running) {
                Menu welcomeMenu = menuController.getMenuByName(WELCOME_MENU_KEY);
                menuController.showMenu(welcomeMenu.getMenuName());
                String userRoleIdentifier = menuController.getUserChoice(reader, welcomeMenu);

                switch (userRoleIdentifier) {
                    case ADMIN -> handleAdminFlow(reader);
                    case CUSTOMER -> handleCustomerFlow(reader);
                    case EXIT -> running = false;
                }
            }
        } catch (IOException e) {
            TECHNICAL_LOGGER.error("Error while reading from console.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles the flow for the admin user, allowing them to perform actions like adding and deleting coworking spaces,
     * and viewing reservations.
     *
     * @param reader The {@code BufferedReader} used to read user input.
     * @throws IOException If an I/O error occurs during user interaction.
     */
    private void handleAdminFlow(BufferedReader reader) throws IOException {
        Admin admin = authorizationController.authenticate(reader, Admin.class);
        boolean logOut = false;
        while (!logOut) {
            Menu adminMenu = menuController.getMenuByName(ADMIN_MENU_KEY);
            menuController.showMenu(adminMenu.getMenuName());
            String adminOptionChoice = menuController.getUserChoice(reader, adminMenu);
            switch (adminOptionChoice) {
                case ADD_COWORKING_SPACE -> coworkingController.add(reader, admin);
                case DELETE_COWORKING_SPACE -> coworkingController.delete(reader, admin);
                case GET_ALL_RESERVATIONS -> reservationController.getAllReservationsByAdmin(admin);
            }
            logOut = shouldLogOut(reader);
        }
    }

    /**
     * Handles the flow for the customer user, allowing them to perform actions like viewing available coworking spaces,
     * adding and managing reservations.
     *
     * @param reader The {@code BufferedReader} used to read customer input.
     * @throws IOException If an I/O error occurs during user interaction.
     */
    private void handleCustomerFlow(BufferedReader reader) throws IOException {
        Customer customer = authorizationController.authenticate(reader, Customer.class);
        boolean logOut = false;
        while (!logOut) {
            Menu customerMenu = menuController.getMenuByName(CUSTOMER_MENU_KEY);
            menuController.showMenu(customerMenu.getMenuName());
            String customerOptionChoice = menuController.getUserChoice(reader, customerMenu);
            switch (customerOptionChoice) {
                case GET_AVAILABLE_COWORKING_SPACES -> coworkingController.getAllSpaces();
                case ADD_RESERVATION -> reservationController.add(reader, customer);
                case GET_RESERVATIONS -> reservationController.getAllReservationsByCustomer(customer);
                case DELETE_RESERVATION -> reservationController.delete(reader, customer);
            }
            logOut = shouldLogOut(reader);
        }
    }

    /**
     * Prompts the user with the next step options (log out or stay) and returns a boolean indicating whether the user
     * should log out.
     *
     * @param reader The {@code BufferedReader} used to read user input.
     * @return {@code true} if the user chose to log out, {@code false} otherwise.
     * @throws IOException If an I/O error occurs during user interaction.
     */
    private boolean shouldLogOut(BufferedReader reader) throws IOException {
        Menu nextStepMenu = menuController.getMenuByName(NEXT_STEP_MENU_KEY);
        menuController.showMenu(nextStepMenu.getMenuName());
        String nextStep = menuController.getUserChoice(reader, nextStepMenu);
        return nextStep.equals(LOG_OUT);
    }
}
