package org.example.coworking.dao;

import org.example.coworking.dao.exception.DaoErrorCode;
import org.example.coworking.dao.exception.MenuNotFoundException;
import org.example.coworking.loader.Loader;
import org.example.coworking.model.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.example.coworking.logger.Log.TECHNICAL_LOGGER;

/**
 * Implementation of {@link MenuDao} for managing menu data storage and retrieval.
 */
@Repository
public class MenuDaoImpl implements MenuDao {
    private final Loader<Menu> menuLoader;
    private static List<Menu> menus;

    /**
     * Constructs a new {@code MenuDaoImpl} with the specified menu loader.
     *
     * @param menuLoader the loader used to retrieve menu data
     */
    @Autowired
    public MenuDaoImpl(Loader<Menu> menuLoader) {
        this.menuLoader = menuLoader;
        loadMenusFromStorage();
    }

    public List<Menu> getMenus() {
        return menus;
    }

    @Override
    public Menu getMenuByName(String name) throws MenuNotFoundException {
        Optional<Menu> possibleMenu = menus.stream()
                .filter(m -> m.getMenuName().equals(name))
                .findFirst();
        if (possibleMenu.isEmpty()) {
            throw new MenuNotFoundException(String.format("Failure to find menu with the name: %s", name), DaoErrorCode.MENU_IS_NOT_FOUND);
        } else {
            return possibleMenu.get();
        }
    }

    private void loadMenusFromStorage() {
        if (menus == null) {
            try {
                menus = menuLoader.load(Menu.class);
            } catch (FileNotFoundException e) {
                TECHNICAL_LOGGER.error("Failure to load Menu List", e);
                throw new RuntimeException("Failure to load Menu List", e);
            }
        }
    }
}
