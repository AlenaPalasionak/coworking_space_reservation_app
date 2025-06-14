package org.example.coworking.service;

import org.example.coworking.repository.exception.EntityNotFoundException;
import org.example.coworking.model.Admin;
import org.example.coworking.model.Customer;
import org.example.coworking.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceImplTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private AuthorizationServiceImpl authorizationService;

    @Test
    void testAuthenticateValidAdmin() throws EntityNotFoundException {
        Admin expectedAdmin = new Admin(1L, "Aden", "123");
        when(userService.getUserByNamePasswordAndAndRole("Aden", "123", Admin.class))
                .thenReturn(expectedAdmin);

        User actualUser = authorizationService.authenticate("Aden", "123", Admin.class);

        assertNotNull(actualUser);
        assertEquals("Aden", actualUser.getName());
        assertEquals("123", actualUser.getPassword());
        assertInstanceOf(Admin.class, actualUser);
        verify(userService).getUserByNamePasswordAndAndRole("Aden", "123", Admin.class);
    }

    @Test
    void testAuthenticateValidCustomer() throws EntityNotFoundException {
        Customer expectedCustomer = new Customer(2L, "Custer", "321");
        when(userService.getUserByNamePasswordAndAndRole("Custer", "321", Customer.class))
                .thenReturn(expectedCustomer);

        User actualUser = authorizationService.authenticate("Custer", "321", Customer.class);

        assertNotNull(actualUser);
        assertEquals("Custer", actualUser.getName());
        assertEquals("321", actualUser.getPassword());
        assertInstanceOf(Customer.class, actualUser);
        verify(userService).getUserByNamePasswordAndAndRole("Custer", "321", Customer.class);
    }

    @Test
    void testAuthenticateWithWrongPasswordThrowsException() throws EntityNotFoundException {
        when(userService.getUserByNamePasswordAndAndRole("Aden", "wrongPass", Admin.class))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () ->
                authorizationService.authenticate("Aden", "wrongPass", Admin.class)
        );
    }

    @Test
    void testAuthenticateWithWrongRoleThrowsException() throws EntityNotFoundException {
        when(userService.getUserByNamePasswordAndAndRole("Aden", "123", Customer.class))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () ->
                authorizationService.authenticate("Aden", "123", Customer.class)
        );
    }
}