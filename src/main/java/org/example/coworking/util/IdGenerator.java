package org.example.coworking.util;

public class IdGenerator {
    private static int userId = 0;
    private static int coworkingId = 0;
    private static int reservationId = 0;

    public static int generateUserId() {
        return userId++;
    }

    public static int generateCoworkingId() {
        return coworkingId++;
    }

    public static int generateReservationId() {
        return reservationId++;
    }
}
