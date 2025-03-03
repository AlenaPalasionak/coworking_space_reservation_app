package org.example.coworking.infrastructure.factory;

import org.example.coworking.infrastructure.controller.AuthorizationController;
import org.example.coworking.infrastructure.controller.CoworkingController;
import org.example.coworking.infrastructure.controller.ReservationController;
import org.example.coworking.infrastructure.dao.*;
import org.example.coworking.infrastructure.json_loader.CoworkingSpaceJsonLoader;
import org.example.coworking.infrastructure.json_loader.JsonLoader;
import org.example.coworking.infrastructure.json_loader.ReservationJsonLoader;
import org.example.coworking.infrastructure.json_loader.UserJsonLoader;
import org.example.coworking.service.*;

public class AppFactory {
    private static final JsonLoader userJsonLoader = new UserJsonLoader();
    private static final JsonLoader coworkingJsonLoader = new CoworkingSpaceJsonLoader();
    private static final JsonLoader reservationJsonLoader = new ReservationJsonLoader();

    private static final UserDao userDao = new UserDaoImpl(userJsonLoader);
    private static final CoworkingDao coworkingDao = new CoworkingDaoImpl(coworkingJsonLoader);
    private static final ReservationDao reservationDao = new ReservationDaoImpl(reservationJsonLoader);

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