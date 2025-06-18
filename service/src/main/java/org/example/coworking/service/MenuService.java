package org.example.coworking.service;

import org.example.coworking.repository.exception.MenuNotFoundException;
import org.example.coworking.entity.Menu;

import java.util.List;

/**
 * This interface defines the operations for managing and retrieving menus in the system.
 * It provides methods to retrieve menus, get specific menu details, check user choices,
 * and find menus by their name.
 */
public interface MenuService {

    /**
     * Retrieves the text representation of a menu based on its name.
     *
     * @param menuName the name of the menu whose text is to be retrieved
     * @return the text associated with the specified menu name
     */
    String getMenuTextByMenuName(String menuName);

    /**
     * Checks if the user's choice matches one of the possible choices for the given menu.
     *
     * @param menu the {@link Menu} object to check the user's choice against
     * @param userChoice the choice made by the user
     * @return true if the user's choice matches one of the possible choices, false otherwise
     */
    boolean isMatchingOneOfPossibleChoices(Menu menu, String userChoice);

    /**
     * Retrieves a menu by its name.
     *
     * @param name the name of the menu to be retrieved
     * @return the {@link Menu} object associated with the specified name
     * @throws MenuNotFoundException if no menu with the given name is found
     */
    Menu getMenuByName(String name) throws MenuNotFoundException;
    List<Menu> getMenus();
}
