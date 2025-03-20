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

/**
 * The AppFactory class is responsible for creating and initializing controllers
 * that interact with the application's various services and data access objects (DAOs).
 * It centralizes the creation of services and their dependencies, ensuring that controllers
 * are initialized with the necessary components, such as DAOs, services, and mappers.
 * This class adheres to the Factory design pattern to promote clean and maintainable code.
 * The factory provides methods to create controllers for handling user authorization,
 * coworking spaces, reservations, and menus. These controllers are wired with the
 * appropriate services and logic required for their operation.
 */
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

    /**
     * Creates and returns an instance of the AuthorizationController, which handles user authorization.
     *
     * @return a new AuthorizationController instance
     */
    public AuthorizationController createAuthorizationController() {
        return new AuthorizationController(authorizationService);
    }

    /**
     * Creates and returns an instance of the CoworkingController, which handles operations related to coworking spaces.
     *
     * @return a new CoworkingController instance
     */
    public CoworkingController createCoworkingController() {
        return new CoworkingController(coworkingService, coworkingMapper);
    }

    /**
     * Creates and returns an instance of the ReservationController, which handles reservation operations.
     *
     * @return a new ReservationController instance
     */
    public ReservationController createReservationController() {
        return new ReservationController(coworkingService, reservationService, reservationMapper);
    }

    /**
     * Creates and returns an instance of the MenuController, which handles menu-related operations.
     *
     * @return a new MenuController instance
     */
    public MenuController createMenuController() {
        return new MenuController(menuService);
    }
}
