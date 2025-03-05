package org.example.coworking.infrastructure.factory;

import org.example.coworking.infrastructure.controller.AuthorizationController;
import org.example.coworking.infrastructure.controller.CoworkingController;
import org.example.coworking.infrastructure.controller.ReservationController;
import org.example.coworking.infrastructure.dao.*;
import org.example.coworking.infrastructure.loader.CoworkingSpaceLoader;
import org.example.coworking.infrastructure.loader.Loader;
import org.example.coworking.infrastructure.loader.ReservationLoader;
import org.example.coworking.infrastructure.loader.UserLoader;
import org.example.coworking.model.CoworkingSpace;
import org.example.coworking.model.Reservation;
import org.example.coworking.model.User;
import org.example.coworking.service.*;

public class AppFactory {
    private static final Loader<User> USER_LOADER = new UserLoader();
    private static final Loader<CoworkingSpace> COWORKING_LOADER = new CoworkingSpaceLoader();
    private static final Loader<Reservation> RESERVATION_LOADER = new ReservationLoader();

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