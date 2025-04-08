package org.example;

import org.example.coworking.controller.AuthorizationController;
import org.example.coworking.controller.CoworkingController;
import org.example.coworking.controller.MenuController;
import org.example.coworking.controller.ReservationController;
import org.example.coworking.factory.AppFactory;
import org.example.coworking.model.Menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.example.coworking.logger.Log.TECHNICAL_LOGGER;

/**
 * The Main class is the entry point of the application. It initializes various controllers
 * for handling different aspects of the system such as reservations, authorization, coworking spaces,
 * and menus.
 */
public class Main {
    private static final String WELCOME_MENU_KEY = "Welcome Menu";
    private static final String ADMIN = "1";
    private static final String CUSTOMER = "2";
    private static final String EXIT = "0";

    public static void main(String[] args) {
        AppFactory appFactory = new AppFactory("JPA");
        ReservationController reservationController = appFactory.createReservationController();
        AuthorizationController authorizationController = appFactory.createAuthorizationController();
        CoworkingController coworkingController = appFactory.createCoworkingController();
        MenuController menuController = appFactory.createMenuController();

        menuController.getMenusFromStorage();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            label:
            while (true) {
                Menu welcomeMenu = menuController.getMenuByName(WELCOME_MENU_KEY);
                menuController.showMenu(welcomeMenu.getMenuName());
                String userRoleIdentifier = menuController.getUserChoice(reader, welcomeMenu);

                switch (userRoleIdentifier) {
                    case ADMIN ->
                            menuController.handleAdminFlow(authorizationController, menuController, coworkingController,
                                    reservationController, reader);
                    case CUSTOMER ->
                            menuController.handleCustomerFlow(authorizationController, menuController, coworkingController,
                                    reservationController, reader);
                    case EXIT -> {
                        System.exit(0);
                        break label;
                    }
                }
            }

        } catch (IOException e) {
            TECHNICAL_LOGGER.error("Error while reading from console. " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
