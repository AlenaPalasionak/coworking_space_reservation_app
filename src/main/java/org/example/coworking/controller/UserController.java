package org.example.coworking.controller;

import org.example.coworking.model.User;
import org.example.coworking.service.*;

import java.io.IOException;
import java.util.Optional;

public class UserController {
    protected static final CoworkingServise coworkingService = new CoworkingServiceImpl();
    protected static final ReservationService reservationService = new ReservationServiceImpl();
    protected static final UserService userService = new UserServiceImpl();

    public <T extends User> Optional<T> authenticate(String name, String password, Class<T> roleClass) throws IOException {
        return userService.authenticate(name, password, roleClass)
                .map(user -> (T) user);
    }
}

