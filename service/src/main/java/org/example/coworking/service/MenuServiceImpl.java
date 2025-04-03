package org.example.coworking.service;

import org.example.coworking.dao.MenuDao;
import org.example.coworking.dao.exception.MenuNotFoundException;
import org.example.coworking.model.Menu;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.example.coworking.logger.Log.TECHNICAL_LOGGER;
import static org.example.coworking.logger.Log.USER_OUTPUT_LOGGER;

public class MenuServiceImpl implements MenuService {

    private final MenuDao menuDao;

    public MenuServiceImpl(MenuDao menuDao) {
        this.menuDao = menuDao;
    }

    @Override
    public List<Menu> getMenusFromStorage() {
        return menuDao.getMenusFromStorage();
    }

    @Override
    public String getMenuTextByMenuName(String menuName) {
        Optional<Menu> possibleMenu = getMenusFromStorage().stream()
                .filter(m -> m.getMenuName().equals(menuName))
                .findFirst();
        if (possibleMenu.isPresent()) {
            return possibleMenu.get().getMenuText();
        } else {
            USER_OUTPUT_LOGGER.warn("Menu text is not found of " + menuName);
            TECHNICAL_LOGGER.warn("Menu text is not found of " + menuName);
            return "Menu text is not found";
        }
    }

    public boolean isMatchingOneOfPossibleChoices(Menu menu, String userChoice) {
        return Arrays.asList(menu.getPossibleChoices()).contains(userChoice);
    }

    @Override
    public Menu getMenuByName(String name) throws MenuNotFoundException {
        return menuDao.getMenuByName(name);
    }
}