//package org.example.coworking.service;
//
//import org.example.coworking.entity.Admin;
//import org.example.coworking.entity.Customer;
//import org.example.coworking.entity.User;
//import org.example.coworking.repository.UserRepository;
//import org.example.coworking.repository.exception.DaoErrorCode;
//import org.example.coworking.repository.exception.EntityNotFoundException;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceImplTest {
//    @Mock
//    private UserRepository userDao;
//    @InjectMocks
//    private UserServiceImpl userService;
//
//    @Test
//    void testGetUserByNamePasswordAndRoleReturnsUser() throws EntityNotFoundException {
//        String name = "Aden";
//        String password = "123";
//        Class<Admin> roleClass = Admin.class;
//        Admin mockAdmin = new Admin();
//        mockAdmin.setName(name);
//        mockAdmin.setPassword(password);
//
//        when(userDao.getUserByNamePasswordAndRole(name, password, roleClass)).thenReturn(mockAdmin);
//
//        User actualUser = userService.getUserByNamePasswordAndAndRole(name, password, roleClass);
//
//        assertEquals(mockAdmin, actualUser);
//        verify(userDao, times(1)).getUserByNamePasswordAndRole(name, password, roleClass);
//    }
//
//    @Test
//    void testGetUserByNamePasswordAndRoleThrowsException() throws EntityNotFoundException {
//        String name = "Unknown";
//        String password = "wrongPass";
//        Class<? extends User> roleClass = Customer.class;
//
//        when(userDao.getUserByNamePasswordAndRole(name, password, roleClass)).thenThrow(new EntityNotFoundException
//                ("User is not found", DaoErrorCode.USER_IS_NOT_FOUND));
//
//        assertThrows(EntityNotFoundException.class, () ->
//                userService.getUserByNamePasswordAndAndRole(name, password, roleClass));
//
//        verify(userDao, times(1)).getUserByNamePasswordAndRole(name, password, roleClass);
//    }
//}
