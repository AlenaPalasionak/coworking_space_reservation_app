package org.example.coworking.infrastructure.service;

import java.time.LocalDateTime;

public class InvalidTimeReservationException extends Throwable {
    public InvalidTimeReservationException(LocalDateTime startTime, LocalDateTime endTime) {
        super("Error in startTime or endTime: \n" + " Start time = " + startTime + ".\n End time = " + endTime);
    }
}
