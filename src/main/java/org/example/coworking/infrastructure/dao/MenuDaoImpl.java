package org.example.coworking.infrastructure.dao;

import org.example.coworking.infrastructure.loader.Loader;
import org.example.coworking.model.Menu;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.example.coworking.infrastructure.logger.Log.TECHNICAL_LOGGER;

public class MenuDaoImpl implements MenuDao {
    private final Loader<Menu> menuLoader;
    private static List<Menu> menus;

    public MenuDaoImpl(Loader<Menu> menuLoader) {
        this.menuLoader = menuLoader;
    }

    @Override
    public List<Menu> getMenusFromStorage() {
        if (menus == null) {
            loadMenuFromStorage();
        }
        return menus;
    }

    @Override
    public Optional<Menu> getMenuByName(String name) {
        return menus.stream()
                .filter(m -> m.getMenuName().equals(name))
                .findFirst();
    }

    private void loadMenuFromStorage() {
        try {
            menus = menuLoader.load(Menu.class);
        } catch (FileNotFoundException e) {
            TECHNICAL_LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
