package org.example.coworking.infrastructure.dao;

public class IdGenerator {

    private static long coworkingId = 0;
    private static long reservationId = 0;
    public static long generateCoworkingId() {
        return coworkingId++;
    }
    public static long generateReservationId() {
        return reservationId++;
    }
}
