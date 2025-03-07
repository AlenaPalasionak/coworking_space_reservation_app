package org.example.coworking.service;

import org.apache.logging.log4j.Logger;
import org.example.coworking.service.exception.MenuNotFoundException;
import org.example.coworking.infrastructure.dao.MenuDao;
import org.example.coworking.infrastructure.logger.Log;
import org.example.coworking.model.Menu;

import java.util.List;
import java.util.Optional;

public class MenuServiceImpl implements MenuService {
    private static final Logger logger = Log.getLogger(MenuServiceImpl.class);

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
            logger.warn("Menu text is not found of " + menuName + "is not found.\n");
            return "Menu text is not found";
        }
    }

    public boolean doesMatchOneOfPossibleChoices(Menu menu, String userChoice) {
        for (String possibleChoice : menu.getPossibleChoices()) {
            if (possibleChoice.equals(userChoice)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Menu getMenuByName(String name) throws MenuNotFoundException {
        Optional<Menu> possibleMenu = menuDao.getMenuByName(name);
        if (possibleMenu.isPresent()) {
            return possibleMenu.get();
        } else throw new MenuNotFoundException(name);
    }
}


