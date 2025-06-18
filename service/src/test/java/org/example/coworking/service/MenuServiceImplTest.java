package org.example.coworking.service;

import org.example.coworking.repository.MenuRepository;
import org.example.coworking.repository.exception.RepositoryErrorCode;
import org.example.coworking.repository.exception.MenuNotFoundException;
import org.example.coworking.entity.Menu;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuServiceImplTest {
    @Mock
    private MenuRepository menuRepository;
    @InjectMocks
    private MenuServiceImpl menuService;

    @Test
    void testGetMenus() {
        List<Menu> expectedMenus = List.of(
                new Menu("Welcome Menu", "Main menu text", new String[]{"1", "2"}),
                new Menu("Admin Menu", "Admin Menu text", new String[]{"A", "B"})
        );
        when(menuRepository.getMenus()).thenReturn(expectedMenus);

        List<Menu> actualMenus = menuService.getMenus();

        assertEquals(expectedMenus, actualMenus);
        verify(menuRepository, times(1)).getMenus();
    }

    @Test
    void testGetMenuTextByMenuNameReturnsCorrectText() {
        Menu menu = new Menu("Welcome Menu", "Welcome menu text", new String[]{"1", "2"});
        when(menuRepository.getMenus()).thenReturn(List.of(menu));

        String menuText = menuService.getMenuTextByMenuName("Welcome Menu");

        assertEquals("Welcome menu text", menuText);
    }

    @Test
    void testGetMenuTextByMenuNameReturnsNotFoundMessage() {
        when(menuRepository.getMenus()).thenReturn(List.of());

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
        when(menuRepository.getMenuByName("Welcome menu")).thenReturn(expectedMenu);

        Menu actualMenu = menuService.getMenuByName("Welcome menu");

        assertEquals(expectedMenu, actualMenu);
    }

    @Test
    void testGetMenuByNameThrowsExceptionWhenMenuNotFound() throws MenuNotFoundException {
        when(menuRepository.getMenuByName("Non Existing Menu"))
                .thenThrow(new MenuNotFoundException("Failure to find menu with the name: Non Existing Menu"
                        , RepositoryErrorCode.MENU_IS_NOT_FOUND));

        assertThrows(MenuNotFoundException.class, () -> menuService.getMenuByName("Non Existing Menu"));

        verify(menuRepository, times(1)).getMenuByName("Non Existing Menu");
    }
}
