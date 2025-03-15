package org.example.coworking.infrastructure.dao;

public class IdGenerator {

    private static Long coworkingId = 1L;
    private static Long reservationId = 1L;
    public static Long generateCoworkingId() {
        return coworkingId++;
    }
    public static Long generateReservationId() {
        return reservationId++;
    }
}
