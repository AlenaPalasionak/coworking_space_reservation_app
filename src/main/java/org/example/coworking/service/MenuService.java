package org.example.coworking.service;

import org.example.coworking.model.Menu;
import org.example.coworking.service.exception.MenuNotFoundException;

import java.util.List;

public interface MenuService {
    List<Menu> getMenusFromStorage();
    String getMenuTextByMenuName(String menuName);
    boolean doesMatchOneOfPossibleChoices(Menu menu, String userChoice);
    public Menu getMenuByName(String name) throws MenuNotFoundException;
}
