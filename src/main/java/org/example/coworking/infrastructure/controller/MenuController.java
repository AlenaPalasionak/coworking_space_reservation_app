package org.example.coworking.infrastructure.controller;

import org.apache.logging.log4j.Logger;
import org.example.coworking.infrastructure.logger.Log;
import org.example.coworking.model.Admin;
import org.example.coworking.model.Customer;
import org.example.coworking.model.Menu;
import org.example.coworking.model.User;
import org.example.coworking.service.MenuService;
import org.example.coworking.service.exception.MenuNotFoundException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Optional;

public class MenuController {
    private static final Logger logger = Log.getLogger(MenuController.class);
    private User user;
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

    public void showMenu(BufferedWriter writer, String menuName) throws IOException {
        String menuText = menuService.getMenuTextByMenuName(menuName);
        writer.write(menuText);
        writer.flush();
    }

    public String getUserChoice(BufferedReader reader, BufferedWriter writer, Menu menu) throws IOException {
        String userChoice;
        do {
            userChoice = reader.readLine();
            if (!menuService.doesMatchOneOfPossibleChoices(menu, userChoice)) {
                logger.info("Wrong number: " + userChoice);
                writer.write("Wrong number: " + userChoice + "\nTry again\n");
                writer.flush();
            }
        } while (!menuService.doesMatchOneOfPossibleChoices(menu, userChoice));
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
                                BufferedReader reader, BufferedWriter writer) throws IOException {
        Optional<User> possibleAdmin = authorizationController.authenticate(reader, writer, Admin.class);
        if (possibleAdmin.isPresent()) {
            user = possibleAdmin.get();
            boolean logOut = false;
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
                logOut = handleNextStep(menuController, coworkingController, reservationController, reader, writer);
            }
        }
    }

    public void handleCustomerFlow(AuthorizationController authorizationController, MenuController menuController,
                                   CoworkingController coworkingController, ReservationController reservationController,
                                   BufferedReader reader, BufferedWriter writer) throws IOException {
        Optional<User> possibleCustomer = authorizationController.authenticate(reader, writer, Customer.class);
        if (possibleCustomer.isPresent()) {
            user = possibleCustomer.get();
            boolean logOut = false;
            while (!logOut) {
                Menu customerMenu = menuController.getMenuByName(CUSTOMER_MENU_KEY);
                menuController.showMenu(writer, customerMenu.getMenuName());
                String customerOptionChoice = menuController.getUserChoice(reader, writer, customerMenu);
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
                logOut = handleNextStep(menuController, coworkingController, reservationController, reader, writer);
            }
        }
    }

    public boolean handleNextStep(MenuController menuController, CoworkingController coworkingController,
                                  ReservationController reservationController, BufferedReader reader,
                                  BufferedWriter writer) throws IOException {
        Menu nextStepMenu = menuController.getMenuByName(NEXT_STEP_MENU_KEY);
        menuController.showMenu(writer, nextStepMenu.getMenuName());
        String nextStep = menuController.getUserChoice(reader, writer, nextStepMenu);
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