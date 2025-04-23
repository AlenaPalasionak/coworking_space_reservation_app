package org.example.coworking.service;

import org.example.coworking.repository.MenuRepository;
import org.example.coworking.repository.exception.MenuNotFoundException;
import org.example.coworking.entity.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.example.coworking.logger.Log.TECHNICAL_LOGGER;
import static org.example.coworking.logger.Log.USER_OUTPUT_LOGGER;

@Service
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    @Autowired
    public MenuServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public String getMenuTextByMenuName(String menuName) {
        Optional<Menu> possibleMenu = getMenus().stream()
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
        return menuRepository.getMenuByName(name);
    }

    @Override
    public List<Menu> getMenus() {
        return menuRepository.getMenus();
    }
}