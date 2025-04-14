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

    private final Loader<User> userLoader;
    private final Loader<CoworkingSpace> coworkingSpaceLoader;
    private final Loader<Reservation> reservationLoader;
    private final Loader<Menu> menuLoader;

    private final UserDao userDao;
    private final CoworkingDao coworkingDao;
    private final ReservationDao reservationDao;
    private final MenuDao menuDao;

    private final TimeLogicValidator timeLogicValidator;
    private final UserService userService;
    private final CoworkingService coworkingService;
    private final ReservationService reservationService;
    private final AuthorizationService authorizationService;

    private final CoworkingMapper coworkingMapper;
    private final ReservationMapper reservationMapper;
    private final MenuService menuService;

    /**
     * Constructs an AppFactory instance based on the given storage type.
     *
     * @param storageType The type of storage to be used ("DB" or "FILE").
     */
    public AppFactory(String storageType) {

        this.userLoader = new UserLoader(userPath);
        this.coworkingSpaceLoader = new CoworkingSpaceLoader(coworkingPlacesPath);
        this.reservationLoader = new ReservationLoader(reservationPath);
        this.menuLoader = new MenuLoader(menuPath);

        this.menuDao = new MenuDaoImpl(menuLoader);
        switch (storageType) {
            case "JDBC" -> {
                this.userDao = new JdbcUserDao();
                this.coworkingDao = new JdbcCoworkingDao();
                this.reservationDao = new JdbcReservationDao();
            }
            case "FILE" -> {
                this.userDao = new FileUserDao(userLoader);
                this.reservationDao = new FileReservationDao(reservationLoader);
                this.coworkingDao = new FileCoworkingDao(coworkingSpaceLoader, reservationDao);
                registerShutdownHook();
            }
            case "JPA" -> {
                this.userDao = new JpaUserDao();
                this.coworkingDao = new JpaCoworkingDao();
                this.reservationDao = new JpaReservationDao();
            }
            default -> throw new IllegalArgumentException("Unknown storage type: " + storageType);
        }
        this.timeLogicValidator = new TimeLogicValidator();
        this.userService = new UserServiceImpl(userDao);
        this.coworkingService = new CoworkingServiceImpl(coworkingDao);
        this.reservationService = new ReservationServiceImpl(reservationDao, coworkingService, timeLogicValidator);
        this.authorizationService = new AuthorizationServiceImpl(userService);

        this.coworkingMapper = new CoworkingMapper();
        this.reservationMapper = new ReservationMapper();
        this.menuService = new MenuServiceImpl(menuDao);

    }

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

    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (coworkingDao instanceof FileCoworkingDao) {
                ((FileCoworkingDao) coworkingDao).shutdown();
            }
            if (reservationDao instanceof FileReservationDao) {
                ((FileReservationDao) reservationDao).shutdown();
            }
        }));
    }
}
