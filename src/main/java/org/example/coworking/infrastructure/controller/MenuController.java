package org.example.coworking.infrastructure.controller;

import org.example.coworking.model.Admin;
import org.example.coworking.model.Customer;
import org.example.coworking.model.Menu;
import org.example.coworking.model.User;
import org.example.coworking.service.MenuService;
import org.example.coworking.infrastructure.dao.exception.MenuNotFoundException;

import java.io.BufferedReader;
import java.io.IOException;

import static org.example.coworking.infrastructure.logger.Log.TECHNICAL_LOGGER;
import static org.example.coworking.infrastructure.logger.Log.USER_OUTPUT_LOGGER;

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
        USER_OUTPUT_LOGGER.info(menuText);
    }

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

    public void getMenusFromStorage() {
        menuService.getMenusFromStorage();
    }

    public Menu getMenuByName(String name) {
        try {
            return menuService.getMenuByName(name);
        } catch (MenuNotFoundException e) {
            USER_OUTPUT_LOGGER.error(e.getMessage());
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

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