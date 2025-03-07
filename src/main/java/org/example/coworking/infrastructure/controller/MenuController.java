package org.example.coworking.infrastructure.controller;

import org.example.coworking.model.Menu;
import org.example.coworking.service.MenuService;
import org.example.coworking.service.exception.MenuNotFoundException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class MenuController {
    MenuService menuService;

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
}