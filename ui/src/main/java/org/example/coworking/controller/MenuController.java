package org.example.coworking.controller;

import org.example.coworking.dao.exception.MenuNotFoundException;
import org.example.coworking.model.Menu;
import org.example.coworking.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
@Component
public class MenuController {
    private final MenuService menuService;

    /**
     * Constructs a {@code MenuController} with required controllers and menu service.
     * @param menuService provides menu data
     */
    @Autowired
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
     * Reads the user's input and validates it against the possible choices of the given menu.
     * If the input is invalid, prompts the user to try again until a valid choice is entered.
     *
     * @param reader the {@code BufferedReader} used to read user input
     * @param menu   the {@code Menu} containing the valid choices
     * @return the validated user choice
     * @throws IOException if an I/O error occurs while reading user input
     */
    public String getUserChoice(BufferedReader reader, Menu menu) throws IOException {
        String userChoice;
        while (true) {
            userChoice = reader.readLine();
            if (menuService.isMatchingOneOfPossibleChoices(menu, userChoice)) {
                return userChoice;
            }
            USER_OUTPUT_LOGGER.warn("You entered the wrong symbol: " + userChoice + ". Try again\n");
        }
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
}
