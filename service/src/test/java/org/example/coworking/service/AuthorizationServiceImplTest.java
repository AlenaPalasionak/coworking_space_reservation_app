package org.example.coworking.service;

import org.example.coworking.dao.exception.UserNotFoundException;
import org.example.coworking.model.Admin;
import org.example.coworking.model.Customer;
import org.example.coworking.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceImplTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private AuthorizationServiceImpl authorizationService;


    @Test
    void testAuthenticateValidAdmin() throws UserNotFoundException {
        User expectedAdmin = new Admin(1L, "Aden", "123");
        List<User> users = List.of(expectedAdmin);
        when(userService.load()).thenReturn(users);

        User actualUser = authorizationService.authenticate("Aden", "123", Admin.class);

        assertNotNull(actualUser);
        assertEquals("Aden", actualUser.getName());
        assertEquals("123", actualUser.getPassword());
        assertInstanceOf(Admin.class, actualUser);
        verify(userService, times(1)).load();
    }

    @Test
    void testAuthenticateValidCustomer() throws UserNotFoundException {
        User expectedCustomer = new Customer(2L, "Custer", "321");
        List<User> users = List.of(expectedCustomer);
        when(userService.load()).thenReturn(users);

        User actualUser = authorizationService.authenticate("Custer", "321", Customer.class);

        assertNotNull(actualUser);
        assertEquals("Custer", actualUser.getName());
        assertEquals("321", actualUser.getPassword());
        assertInstanceOf(Customer.class, actualUser);

        verify(userService, times(1)).load();
    }

    @Test
    void testAuthenticateWithWrongPasswordThrowsException() {
        User expectedAdmin = new Admin(1L, "Aden", "123");
        List<User> users = List.of(expectedAdmin);
        when(userService.load()).thenReturn(users);

        assertThrows(UserNotFoundException.class, () ->
                authorizationService.authenticate("Aden", "wrongPass", Admin.class)
        );
    }

    @Test
    void testAuthenticateWithWrongRoleThrowsException() {
        User expectedAdmin = new Admin(1L, "Aden", "123");
        List<User> users = List.of(expectedAdmin);
        when(userService.load()).thenReturn(users);

        assertThrows(UserNotFoundException.class, () ->
                authorizationService.authenticate("Aden", "123", Customer.class)
        );
    }
}

