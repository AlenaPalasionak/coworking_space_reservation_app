package org.example.coworking.service;

import org.example.coworking.model.Menu;
import org.example.coworking.dao.exception.MenuNotFoundException;

import java.util.List;

public interface MenuService {
    List<Menu> getMenusFromStorage();
    String getMenuTextByMenuName(String menuName);
    boolean isMatchingOneOfPossibleChoices(Menu menu, String userChoice);
    Menu getMenuByName(String name) throws MenuNotFoundException;
}
