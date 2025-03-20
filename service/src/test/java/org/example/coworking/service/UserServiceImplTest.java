package org.example.coworking.service;

import org.example.coworking.dao.UserDao;
import org.example.coworking.model.Admin;
import org.example.coworking.model.Customer;
import org.example.coworking.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserDao userDao;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testLoadReturnsUsers() {
        List<User> expectedUsers = List.of(
                new Admin(1L, "Aden", "123"),
                new Customer(2L, "Custer", "321")
        );
        when(userDao.load()).thenReturn(expectedUsers);

        List<User> actualUsers = userService.load();

        assertEquals(expectedUsers, actualUsers);
        verify(userDao, times(1)).load();
    }

    @Test
    void testLoadReturnsEmptyListWhenNoUsers() {
        when(userDao.load()).thenReturn(List.of());

        List<User> actualUsers = userService.load();

        assertEquals(0, actualUsers.size());
        verify(userDao, times(1)).load();
    }
}