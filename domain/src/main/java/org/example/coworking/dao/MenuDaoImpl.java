package org.example.coworking.dao;

import org.example.coworking.loader.Loader;
import org.example.coworking.model.Menu;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.example.coworking.logger.Log.TECHNICAL_LOGGER;

/**
 * Implementation of {@link MenuDao} for managing menu data storage and retrieval.
 */
public class MenuDaoImpl implements MenuDao {
    private final Loader<Menu> menuLoader;
    private static List<Menu> menus;

    /**
     * Constructs a new {@code MenuDaoImpl} with the specified menu loader.
     *
     * @param menuLoader the loader used to retrieve menu data
     */
    public MenuDaoImpl(Loader<Menu> menuLoader) {
        this.menuLoader = menuLoader;
    }

    /**
     * Retrieves the list of menus from storage. Loads the data if not already cached.
     *
     * @return a list of {@link Menu} objects
     */
    @Override
    public List<Menu> getMenusFromStorage() {
        if (menus == null) {
            loadMenuFromStorage();
        }
        return menus;
    }

    /**
     * Finds a menu by its name.
     *
     * @param name the name of the menu to search for
     * @return an {@link Optional} containing the found menu, or empty if not found
     */
    @Override
    public Optional<Menu> getMenuByName(String name) {
        return menus.stream()
                .filter(m -> m.getMenuName().equals(name))
                .findFirst();
    }

    /**
     * Loads menu data from storage using the provided loader.
     * Throws a {@link RuntimeException} if the file is not found.
     */
    private void loadMenuFromStorage() {
        try {
            menus = menuLoader.load(Menu.class);
        } catch (FileNotFoundException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
