package org.example.coworking.infrastructure.factory;

import org.example.coworking.infrastructure.controller.AuthorizationController;
import org.example.coworking.infrastructure.controller.CoworkingController;
import org.example.coworking.infrastructure.controller.ReservationController;
import org.example.coworking.infrastructure.dao.*;
import org.example.coworking.infrastructure.json_loader.CoworkingSpaceLoader;
import org.example.coworking.infrastructure.json_loader.Loader;
import org.example.coworking.infrastructure.json_loader.ReservationLoader;
import org.example.coworking.infrastructure.json_loader.UserLoader;
import org.example.coworking.service.*;

public class AppFactory {
    private static final Loader USER_LOADER = new UserLoader();
    private static final Loader COWORKING_LOADER = new CoworkingSpaceLoader();
    private static final Loader RESERVATION_LOADER = new ReservationLoader();

    private static final UserDao userDao = new UserDaoImpl(USER_LOADER);
    private static final CoworkingDao coworkingDao = new CoworkingDaoImpl(COWORKING_LOADER);
    private static final ReservationDao reservationDao = new ReservationDaoImpl(RESERVATION_LOADER);

    private static final UserService userService = new UserServiceImpl(userDao);
    private static final CoworkingService coworkingService = new CoworkingServiceImpl(coworkingDao);
    private static final ReservationService reservationService = new ReservationServiceImpl(reservationDao, coworkingDao);
    private static final AuthorizationService authorizationService = new AuthorizationServiceImpl(userService);

    public static AuthorizationController createAuthorizationController() {
        return new AuthorizationController(authorizationService, userService);
    }

    public static CoworkingController createCoworkingController() {
        return new CoworkingController(coworkingService, reservationService);
    }

    public static ReservationController createReservationController() {
        return new ReservationController(coworkingService, reservationService);
    }
}