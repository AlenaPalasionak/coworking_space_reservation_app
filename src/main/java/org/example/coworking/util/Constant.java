package org.example.coworking.util;

public class Constant {
    public static final String WELCOME_MENU = """
                                    
            Welcome to the Coworking Space Reservation!
            If you are an *admin* press 1
            If you are a *customer* press 2
            If you want to exit press 0
                                
            """;
    public static final String CUSTOMER_MENU = """ 
            Press 1 to browse available spaces.
            Press 2 to Make a reservation.
            Press 3 to view your reservations.
            Press 4 to cancel a reservation.
            """;

    public static final String ADMIN_MENU = """ 
            Press 1 to add a new coworking space.
            Press 2 to remove a coworking space.
            Press 3 to view all reservations.
            """;

    public static final String COWORKING_TYPE_MENU = """
            Choose the coworking type (press only one of the numbers):
            Open space coworking - 0
            Private Office - 1
            Coworking+Co-living - 2
            """;
    public static final String FACILITY_MENU = """
            Choose the facilities. Write numbers comma-separated on one line:
            NO Facilities - just press Enter,
            PARKING - 0,
            WIFI - 1,
            KITCHEN - 2,
            PRINTER - 3,
            CONDITIONING - 4
            """;

    public static final String EXIT = "0";
    public static final String ADMIN = "1";
    public static final String CUSTOMER = "2";
    public static final String ADD_COWORKING_SPACE = "1";
    public static final String DELETE_COWORKING_SPACE = "2";
    public static final String GET_ALL_RESERVATIONS = "3";
    public static final String GET_AVAILABLE_COWORKING_SPACES = "1";
    public static final String ADD_RESERVATION = "2";

    public static final String GET_RESERVATIONS = "3";

    public static final String DELETE_RESERVATION = "4";
}
