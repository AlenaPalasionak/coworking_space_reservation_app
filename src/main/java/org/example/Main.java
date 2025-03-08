package org.example;

import org.apache.logging.log4j.Logger;
import org.example.coworking.infrastructure.controller.AuthorizationController;
import org.example.coworking.infrastructure.controller.CoworkingController;
import org.example.coworking.infrastructure.controller.MenuController;
import org.example.coworking.infrastructure.controller.ReservationController;
import org.example.coworking.infrastructure.factory.AppFactory;
import org.example.coworking.infrastructure.logger.Log;
import org.example.coworking.model.Menu;

import java.io.*;

public class Main {
    private static final Logger logger = Log.getLogger(Main.class);
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

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out))) {
            label:
            while (true) {
                Menu welcomeMenu = menuController.getMenuByName(WELCOME_MENU_KEY);
                menuController.showMenu(writer, welcomeMenu.getMenuName());
                String userRoleIdentifier = menuController.getUserChoice(reader, writer, welcomeMenu);
                switch (userRoleIdentifier) {
                    case EXIT:
                        break label;
                    case ADMIN:
                        menuController.handleAdminFlow(authorizationController, menuController, coworkingController, reservationController, reader, writer);
                        break;
                    case CUSTOMER:
                        menuController.handleCustomerFlow(authorizationController, menuController, coworkingController, reservationController, reader, writer);
                        break;
                }
            }
        } catch (IOException e) {
            logger.error("Error while reading or writing from console. " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
