package org.example;

import org.example.coworking.infrastructure.controller.AuthorizationController;
import org.example.coworking.infrastructure.controller.CoworkingController;
import org.example.coworking.infrastructure.controller.MenuController;
import org.example.coworking.infrastructure.controller.ReservationController;
import org.example.coworking.infrastructure.factory.AppFactory;
import org.example.coworking.model.Menu;

import java.io.*;

import static org.example.coworking.infrastructure.logger.Log.CONSOLE_LOGGER;
import static org.example.coworking.infrastructure.logger.Log.FILE_LOGGER;

public class Main {

    private static final String WELCOME_MENU_KEY = "Welcome Menu";
    private static final String EXIT = "0";
    private static final String ADMIN = "1";
    private static final String CUSTOMER = "2";

    public static void main(String[] args) {
        AppFactory appFactory = new AppFactory();
        ReservationController reservationController = appFactory.createReservationController();
        AuthorizationController authorizationController = appFactory.createAuthorizationController();
        CoworkingController coworkingController = appFactory.createCoworkingController();
        MenuController menuController = appFactory.createMenuController();

        coworkingController.load();
        reservationController.load();
        menuController.getMenusFromStorage();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            label:
            while (true) {
                Menu welcomeMenu = menuController.getMenuByName(WELCOME_MENU_KEY);
                menuController.showMenu(welcomeMenu.getMenuName());
                String userRoleIdentifier = menuController.getUserChoice(reader, welcomeMenu);
                switch (userRoleIdentifier) {
                    case EXIT:
                        break label;
                    case ADMIN:
                        menuController.handleAdminFlow(authorizationController, menuController, coworkingController
                                , reservationController, reader);
                        break;
                    case CUSTOMER:
                        menuController.handleCustomerFlow(authorizationController, menuController, coworkingController
                                , reservationController, reader);
                        break;
                }
            }
        } catch (IOException e) {
            FILE_LOGGER.error("Error while reading or writing from console. " + e.getMessage());
            CONSOLE_LOGGER.error("Error while reading or writing from console. " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
