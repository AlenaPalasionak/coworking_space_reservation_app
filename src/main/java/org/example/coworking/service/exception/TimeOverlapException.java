package org.example.coworking.service.exception;

import org.example.coworking.model.ReservationPeriod;

public class TimeOverlapException extends Exception {
    public TimeOverlapException(ReservationPeriod newPeriod) {
        super("Reservation overlaps with an existing period: " + newPeriod);
    }
}
