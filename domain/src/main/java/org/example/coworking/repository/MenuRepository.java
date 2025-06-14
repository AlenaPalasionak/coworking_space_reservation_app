package org.example.coworking.repository;

import org.example.coworking.repository.exception.MenuNotFoundException;
import org.example.coworking.model.Menu;

import java.util.List;

/**
 * Interface for managing menus.
 * Provides methods to retrieve menus from storage.
 */
public interface MenuRepository {

    /**
     * Retrieves a menu by its name.
     *
     * @param name the name of the menu to retrieve.
     * @return an Optional containing the menu if found, or empty if not found.
     */
    Menu getMenuByName(String name) throws MenuNotFoundException;

    List<Menu> getMenus();

}
