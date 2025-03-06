package org.example.coworking.infrastructure.factory;

import org.example.coworking.infrastructure.controller.AuthorizationController;
import org.example.coworking.infrastructure.controller.CoworkingController;
import org.example.coworking.infrastructure.controller.ReservationController;
import org.example.coworking.infrastructure.dao.*;
import org.example.coworking.infrastructure.dao.exception.CoworkingNotFoundException;
import org.example.coworking.infrastructure.dao.exception.ReservationNotFoundException;
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

    private static final UserDao USER_DAO = new UserDaoImpl(USER_LOADER);
    private static final ModelDao<CoworkingSpace, CoworkingNotFoundException> COWORKING_DAO = new CoworkingDaoImpl(COWORKING_LOADER);
    private static final ModelDao<Reservation, ReservationNotFoundException> RESERVATION_DAO = new ReservationDaoImpl(RESERVATION_LOADER);

    private static final UserService USER_SERVICE = new UserServiceImpl(USER_DAO);
    private static final CoworkingService COWORKING_SERVICE = new CoworkingServiceImpl(COWORKING_DAO);
    private static final ReservationService RESERVATION_SERVICE = new ReservationServiceImpl(RESERVATION_DAO, COWORKING_DAO);
    private static final AuthorizationService AUTHORIZATION_SERVICE = new AuthorizationServiceImpl(USER_SERVICE);

    public static AuthorizationController createAuthorizationController() {
        return new AuthorizationController(AUTHORIZATION_SERVICE, USER_SERVICE);
    }

    public static CoworkingController createCoworkingController() {
        return new CoworkingController(COWORKING_SERVICE, RESERVATION_SERVICE);
    }

    public static ReservationController createReservationController() {
        return new ReservationController(COWORKING_SERVICE, RESERVATION_SERVICE);
    }
}