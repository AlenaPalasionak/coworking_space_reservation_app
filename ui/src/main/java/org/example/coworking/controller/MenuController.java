package org.example.coworking.controller;

import org.example.coworking.model.Admin;
import org.example.coworking.model.Customer;
import org.example.coworking.model.Menu;
import org.example.coworking.model.User;
import org.example.coworking.service.MenuService;
import org.example.coworking.dao.exception.MenuNotFoundException;

import java.io.BufferedReader;
import java.io.IOException;

import static org.example.coworking.logger.Log.TECHNICAL_LOGGER;
import static org.example.coworking.logger.Log.USER_OUTPUT_LOGGER;

/**
 * The {@code MenuController} class handles the user interface for both admin and customer flows.
 * It interacts with various services such as {@code MenuService}, {@code CoworkingController}, and {@code ReservationController}
 * to display menus, process user choices, and perform actions like adding, deleting coworking spaces,
 * making reservations, and logging out.
 */
public class MenuController {

    private User user = null;
    private final MenuService menuService;
    private static final String EXIT = "0";
    private static final String LOG_OUT = "2";
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

    /**
     * Constructs a {@code MenuController} with the given {@code MenuService}.
     *
     * @param menuService The service used to retrieve and manage menu data.
     */
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * Displays a menu based on the provided menu name.
     *
     * @param menuName The name of the menu to be displayed.
     * @throws IOException If an I/O error occurs while retrieving the menu.
     */
    public void showMenu(String menuName) throws IOException {
        String menuText = menuService.getMenuTextByMenuName(menuName);
        USER_OUTPUT_LOGGER.info(menuText);
    }

    /**
     * Reads and validates the user's choice from the input, ensuring it matches one of the possible choices for the menu.
     *
     * @param reader The {@code BufferedReader} used to read user input.
     * @param menu The menu to check the user choice against.
     * @return The validated user choice.
     * @throws IOException If an I/O error occurs while reading the user input.
     */
    public String getUserChoice(BufferedReader reader, Menu menu) throws IOException {
        String userChoice;
        do {
            userChoice = reader.readLine();
            if (!menuService.isMatchingOneOfPossibleChoices(menu, userChoice)) {
                USER_OUTPUT_LOGGER.warn("You entered the wrong symbol: " + userChoice + ". Try again\n");
            }
        } while (!menuService.isMatchingOneOfPossibleChoices(menu, userChoice));
        return userChoice;
    }

    /**
     * Loads the menus from storage.
     */
    public void getMenusFromStorage() {
        menuService.getMenusFromStorage();
    }

    /**
     * Retrieves a menu by its name.
     *
     * @param name The name of the menu to retrieve.
     * @return The {@code Menu} object corresponding to the given name.
     * @throws RuntimeException If the menu is not found in the storage.
     */
    public Menu getMenuByName(String name) {
        try {
            return menuService.getMenuByName(name);
        } catch (MenuNotFoundException e) {
            USER_OUTPUT_LOGGER.error(e.getErrorCode());
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Handles the flow for the admin user, allowing them to perform actions like adding and deleting coworking spaces,
     * and viewing reservations.
     *
     * @param authorizationController The controller used for user authentication.
     * @param menuController The controller used to handle menus.
     * @param coworkingController The controller used to manage coworking spaces.
     * @param reservationController The controller used to manage reservations.
     * @param reader The {@code BufferedReader} used to read user input.
     * @throws IOException If an I/O error occurs during user interaction.
     */
    public void handleAdminFlow(AuthorizationController authorizationController, MenuController menuController,
                                CoworkingController coworkingController, ReservationController reservationController,
                                BufferedReader reader) throws IOException {
        user = authorizationController.authenticate(reader, Admin.class);
        boolean logOut = false;
        while (!logOut) {
            Menu adminMenu = menuController.getMenuByName(ADMIN_MENU_KEY);
            menuController.showMenu(adminMenu.getMenuName());
            String adminOptionChoice = menuController.getUserChoice(reader, adminMenu);
            switch (adminOptionChoice) {
                case ADD_COWORKING_SPACE -> coworkingController.add(reader, user);
                case DELETE_COWORKING_SPACE -> coworkingController.delete(reader, user);
                case GET_ALL_RESERVATIONS -> reservationController.getAllReservations(user);
            }
            logOut = shouldLogOut(menuController, coworkingController, reservationController, reader);
        }
    }

    /**
     * Handles the flow for the customer user, allowing them to perform actions like viewing available coworking spaces,
     * adding and managing reservations.
     *
     * @param authorizationController The controller used for user authentication.
     * @param menuController The controller used to handle menus.
     * @param coworkingController The controller used to manage coworking spaces.
     * @param reservationController The controller used to manage reservations.
     * @param reader The {@code BufferedReader} used to read user input.
     * @throws IOException If an I/O error occurs during user interaction.
     */
    public void handleCustomerFlow(AuthorizationController authorizationController, MenuController menuController,
                                   CoworkingController coworkingController, ReservationController reservationController,
                                   BufferedReader reader) throws IOException {
        user = authorizationController.authenticate(reader, Customer.class);
        boolean logOut = false;
        while (!logOut) {
            Menu customerMenu = menuController.getMenuByName(CUSTOMER_MENU_KEY);
            menuController.showMenu(customerMenu.getMenuName());
            String customerOptionChoice = menuController.getUserChoice(reader, customerMenu);
            switch (customerOptionChoice) {
                case GET_AVAILABLE_COWORKING_SPACES -> coworkingController.getAllSpaces(user);
                case ADD_RESERVATION -> reservationController.add(reader, user);
                case GET_RESERVATIONS -> reservationController.getAllReservations(user);
                case DELETE_RESERVATION -> reservationController.delete(reader, user);
            }
            logOut = shouldLogOut(menuController, coworkingController, reservationController, reader);
        }
    }

    /**
     * Prompts the user with the next step options (log out or exit) and returns a boolean indicating whether the user
     * should log out.
     *
     * @param menuController The controller used to handle menus.
     * @param coworkingController The controller used to manage coworking spaces.
     * @param reservationController The controller used to manage reservations.
     * @param reader The {@code BufferedReader} used to read user input.
     * @return {@code true} if the user chose to log out, {@code false} otherwise.
     * @throws IOException If an I/O error occurs during user interaction.
     */
    public boolean shouldLogOut(MenuController menuController, CoworkingController coworkingController,
                                ReservationController reservationController, BufferedReader reader) throws IOException {
        Menu nextStepMenu = menuController.getMenuByName(NEXT_STEP_MENU_KEY);
        menuController.showMenu(nextStepMenu.getMenuName());
        String nextStep = menuController.getUserChoice(reader, nextStepMenu);
        if (nextStep.equals(LOG_OUT)) {
            return true;
        } else if (nextStep.equals(EXIT)) {
            coworkingController.save();
            reservationController.save();
            System.exit(0);
        }
        return false;
    }
}
