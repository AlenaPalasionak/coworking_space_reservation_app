package org.example.coworking.infrastructure.controller;

import org.example.coworking.model.Admin;
import org.example.coworking.model.Customer;
import org.example.coworking.model.Menu;
import org.example.coworking.model.User;
import org.example.coworking.service.MenuService;
import org.example.coworking.service.exception.MenuNotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

import static org.example.coworking.infrastructure.logger.Log.CONSOLE_LOGGER;

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

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    public void showMenu(String menuName) throws IOException {
        String menuText = menuService.getMenuTextByMenuName(menuName);
        CONSOLE_LOGGER.info(menuText);
    }

    public String getUserChoice(BufferedReader reader, Menu menu) throws IOException {
        String userChoice;
        do {
            userChoice = reader.readLine();
            if (!menuService.isMatchingOneOfPossibleChoices(menu, userChoice)) {
                CONSOLE_LOGGER.info("You entered the wrong number: " + userChoice);
            }
        } while (!menuService.isMatchingOneOfPossibleChoices(menu, userChoice));
        return userChoice;
    }

    public void getMenusFromStorage() {
        menuService.getMenusFromStorage();
    }

    public Menu getMenuByName(String name) {
        try {
            return menuService.getMenuByName(name);
        } catch (MenuNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void handleAdminFlow(AuthorizationController authorizationController, MenuController menuController,
                                CoworkingController coworkingController, ReservationController reservationController,
                                BufferedReader reader) throws IOException {
        Optional<User> possibleAdmin = authorizationController.authenticate(reader, Admin.class);
        if (possibleAdmin.isPresent()) {
            user = possibleAdmin.get();
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
                logOut = handleNextStep(menuController, coworkingController, reservationController, reader);
            }
        }
    }

    public void handleCustomerFlow(AuthorizationController authorizationController, MenuController menuController,
                                   CoworkingController coworkingController, ReservationController reservationController,
                                   BufferedReader reader) throws IOException {
        Optional<User> possibleCustomer = authorizationController.authenticate(reader, Customer.class);
        if (possibleCustomer.isPresent()) {
            user = possibleCustomer.get();
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
                logOut = handleNextStep(menuController, coworkingController, reservationController, reader);
            }
        }
    }

    public boolean handleNextStep(MenuController menuController, CoworkingController coworkingController,
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