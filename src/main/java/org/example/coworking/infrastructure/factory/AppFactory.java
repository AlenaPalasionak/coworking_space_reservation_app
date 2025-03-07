package org.example.coworking.infrastructure.factory;

import org.example.coworking.infrastructure.controller.AuthorizationController;
import org.example.coworking.infrastructure.controller.CoworkingController;
import org.example.coworking.infrastructure.controller.MenuController;
import org.example.coworking.infrastructure.controller.ReservationController;
import org.example.coworking.infrastructure.dao.*;
import org.example.coworking.infrastructure.loader.*;
import org.example.coworking.model.CoworkingSpace;
import org.example.coworking.model.Menu;
import org.example.coworking.model.Reservation;
import org.example.coworking.model.User;
import org.example.coworking.service.*;

public class AppFactory {
    private final String menuPath = "src/main/resources/menu.json";
    private final String userPath = "src/main/resources/users.json";
    private final String coworkingPlacesPath = "src/main/resources/coworking_places.json";
    private final String reservationPath = "src/main/resources/reservations.json";
    private final Loader<User> USER_LOADER = new UserLoader(userPath);
    private final Loader<CoworkingSpace> COWORKING_LOADER = new CoworkingSpaceLoader(coworkingPlacesPath);
    private final Loader<Reservation> RESERVATION_LOADER = new ReservationLoader(reservationPath);
    private final Loader<Menu> MENU_LOADER = new MenuLoader(menuPath);

    private final UserDao USER_DAO = new UserDaoImpl(USER_LOADER);
    private final CoworkingDao COWORKING_DAO = new CoworkingDaoImpl(COWORKING_LOADER);
    private final ReservationDao RESERVATION_DAO = new ReservationDaoImpl(RESERVATION_LOADER);
    private final MenuDao MENU_DAO = new MenuDaoImpl(MENU_LOADER);
    private final UserService USER_SERVICE = new UserServiceImpl(USER_DAO);
    private final CoworkingService COWORKING_SERVICE = new CoworkingServiceImpl(COWORKING_DAO);
    private final ReservationService RESERVATION_SERVICE = new ReservationServiceImpl(RESERVATION_DAO);
    private final AuthorizationService AUTHORIZATION_SERVICE = new AuthorizationServiceImpl(USER_SERVICE);
    private final MenuService MENU_SERVICE = new MenuServiceImpl(MENU_DAO);

    public AuthorizationController createAuthorizationController() {
        return new AuthorizationController(AUTHORIZATION_SERVICE, USER_SERVICE);
    }

    public CoworkingController createCoworkingController() {
        return new CoworkingController(COWORKING_SERVICE, RESERVATION_SERVICE);
    }

    public ReservationController createReservationController() {
        return new ReservationController(COWORKING_SERVICE, RESERVATION_SERVICE);
    }

    public MenuController createMenuController() {
        return new MenuController(MENU_SERVICE);
    }
}