package org.example.coworking.infrastructure.factory;

import org.example.coworking.infrastructure.controller.AuthorizationController;
import org.example.coworking.infrastructure.controller.CoworkingController;
import org.example.coworking.infrastructure.controller.MenuController;
import org.example.coworking.infrastructure.controller.ReservationController;
import org.example.coworking.infrastructure.dao.*;
import org.example.coworking.infrastructure.loader.*;
import org.example.coworking.infrastructure.mapper.CoworkingMapper;
import org.example.coworking.infrastructure.mapper.ReservationMapper;
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
    private final Loader<User> userLoader = new UserLoader(userPath);
    private final Loader<CoworkingSpace> coworkingSpaceLoader = new CoworkingSpaceLoader(coworkingPlacesPath);
    private final Loader<Reservation> reservationLoader = new ReservationLoader(reservationPath);
    private final Loader<Menu> menuLoader = new MenuLoader(menuPath);

    private final UserDao userDao = new UserDaoImpl(userLoader);
    private final CoworkingDao coworkingDao = new CoworkingDaoImpl(coworkingSpaceLoader);
    private final ReservationDao reservationDao = new ReservationDaoImpl(reservationLoader);
    private final MenuDao menuDao = new MenuDaoImpl(menuLoader);
    private final UserService userService = new UserServiceImpl(userDao);
    private final CoworkingService coworkingService = new CoworkingServiceImpl(coworkingDao);
    private final ReservationService reservationService = new ReservationServiceImpl(reservationDao);
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