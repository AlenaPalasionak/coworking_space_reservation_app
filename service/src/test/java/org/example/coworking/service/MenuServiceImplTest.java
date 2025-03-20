package org.example.coworking.service;

import org.example.coworking.dao.MenuDao;
import org.example.coworking.dao.exception.MenuNotFoundException;
import org.example.coworking.model.Menu;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuServiceImplTest {
    @Mock
    private MenuDao menuDao;
    @InjectMocks
    private MenuServiceImpl menuService;

    @Test
    void testGetMenusFromStorageReturnsMenus() {
        List<Menu> expectedMenus = List.of(
                new Menu("Welcome Menu", "Main menu text", new String[]{"1", "2"}),
                new Menu("Admin Menu", "Admin Menu text", new String[]{"A", "B"})
        );
        when(menuDao.getMenusFromStorage()).thenReturn(expectedMenus);

        List<Menu> actualMenus = menuService.getMenusFromStorage();

        assertEquals(expectedMenus, actualMenus);
        verify(menuDao, times(1)).getMenusFromStorage();
    }

    @Test
    void testGetMenuTextByMenuNameReturnsCorrectText() {
        Menu menu = new Menu("Welcome Menu", "Welcome menu text", new String[]{"1", "2"});
        when(menuDao.getMenusFromStorage()).thenReturn(List.of(menu));

        String menuText = menuService.getMenuTextByMenuName("Welcome Menu");

        assertEquals("Welcome menu text", menuText);
    }

    @Test
    void testGetMenuTextByMenuNameReturnsNotFoundMessage() {
        when(menuDao.getMenusFromStorage()).thenReturn(List.of());

        String menuText = menuService.getMenuTextByMenuName("Non Existing Menu");

        assertEquals("Menu text is not found", menuText);
    }

    @Test
    void testIsMatchingOneOfPossibleChoicesReturnsTrue() {
        Menu menu = new Menu("Welcome menu", "Welcome menu text", new String[]{"1", "2"});

        assertTrue(menuService.isMatchingOneOfPossibleChoices(menu, "1"));
        assertTrue(menuService.isMatchingOneOfPossibleChoices(menu, "2"));
    }

    @Test
    void testIsMatchingOneOfPossibleChoicesReturnsFalse() {
        Menu menu = new Menu("Welcome menu", "Welcome menu text", new String[]{"1", "2"});

        assertFalse(menuService.isMatchingOneOfPossibleChoices(menu, "3"));
        assertFalse(menuService.isMatchingOneOfPossibleChoices(menu, "A"));
    }

    @Test
    void testGetMenuByNameReturnsMenu() throws MenuNotFoundException {
        Menu expectedMenu = new Menu("Welcome menu", "Welcome menu text", new String[]{"1", "2"});
        when(menuDao.getMenuByName("Welcome menu")).thenReturn(Optional.of(expectedMenu));

        Menu actualMenu = menuService.getMenuByName("Welcome menu");

        assertEquals(expectedMenu, actualMenu);
    }

    @Test
    void testGetMenuByNameThrowsExceptionWhenNotFound() {
        when(menuDao.getMenuByName("Non Existing menu")).thenReturn(Optional.empty());

        assertThrows(MenuNotFoundException.class, () -> menuService.getMenuByName("Non Existing menu"));
    }
}
