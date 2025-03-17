package org.example.coworking.factory;

import org.example.coworking.controller.AuthorizationController;
import org.example.coworking.controller.CoworkingController;
import org.example.coworking.controller.MenuController;
import org.example.coworking.controller.ReservationController;
import org.example.coworking.dao.*;
import org.example.coworking.loader.*;
import org.example.coworking.mapper.CoworkingMapper;
import org.example.coworking.mapper.ReservationMapper;
import org.example.coworking.model.CoworkingSpace;
import org.example.coworking.model.Menu;
import org.example.coworking.model.Reservation;
import org.example.coworking.model.User;
import org.example.coworking.service.*;
import org.example.coworking.service.validator.TimeLogicValidator;

public class AppFactory {
    private final String menuPath = "menu.json";
    private final String userPath = "users.json";
    private final String coworkingPlacesPath = "coworking_places.json";
    private final String reservationPath = "reservations.json";
    private final Loader<User> userLoader = new UserLoader(userPath);
    private final Loader<CoworkingSpace> coworkingSpaceLoader = new CoworkingSpaceLoader(coworkingPlacesPath);
    private final Loader<Reservation> reservationLoader = new ReservationLoader(reservationPath);
    private final Loader<Menu> menuLoader = new MenuLoader(menuPath);

    private final UserDao userDao = new UserDaoImpl(userLoader);
    private final CoworkingDao coworkingDao = new CoworkingDaoImpl(coworkingSpaceLoader);
    private final ReservationDao reservationDao = new ReservationDaoImpl(reservationLoader);
    private final MenuDao menuDao = new MenuDaoImpl(menuLoader);
    private final TimeLogicValidator timeLogicValidator = new TimeLogicValidator();
    private final UserService userService = new UserServiceImpl(userDao);
    private final CoworkingService coworkingService = new CoworkingServiceImpl(coworkingDao);
    private final ReservationService reservationService = new ReservationServiceImpl(reservationDao, coworkingService, timeLogicValidator);
    private final AuthorizationService authorizationService = new AuthorizationServiceImpl(userService);

    private final CoworkingMapper coworkingMapper = new CoworkingMapper();
    private final ReservationMapper reservationMapper = new ReservationMapper();
    private final MenuService menuService = new MenuServiceImpl(menuDao);

    public AuthorizationController createAuthorizationController() {
        return new AuthorizationController(authorizationService);
    }

    public CoworkingController createCoworkingController() {
        return new CoworkingController(coworkingService, coworkingMapper);
    }

    public ReservationController createReservationController() {
        return new ReservationController(coworkingService, reservationService, reservationMapper);
    }

    public MenuController createMenuController() {
        return new MenuController(menuService);
    }
}