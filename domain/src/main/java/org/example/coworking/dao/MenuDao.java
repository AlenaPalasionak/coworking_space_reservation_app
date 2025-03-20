package org.example.coworking.dao;

import org.example.coworking.model.Menu;

import java.util.List;
import java.util.Optional;

/**
 * Interface for managing menus.
 * Provides methods to retrieve menus from storage.
 */
public interface MenuDao {

    /**
     * Retrieves a list of all menus from the storage.
     *
     * @return a list of menus available in the storage.
     */
    List<Menu> getMenusFromStorage();

    /**
     * Retrieves a menu by its name.
     *
     * @param name the name of the menu to retrieve.
     * @return an Optional containing the menu if found, or empty if not found.
     */
    Optional<Menu> getMenuByName(String name);
}
