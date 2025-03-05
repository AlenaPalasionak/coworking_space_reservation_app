package org.example.coworking.infrastructure.util;

import org.example.coworking.infrastructure.util.exception.InvalidTimeReservationException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ReservationTimeValidator {

    public static void validateReservation(LocalDateTime startTime, LocalDateTime endTime) throws InvalidTimeReservationException {
        validateNotNull(startTime, endTime);
        validateDatesNotInPast(startTime, endTime);
        validateStartNotAfterEnd(startTime, endTime);
        validateDurationAtLeastOneHour(startTime, endTime);
    }

    private static void validateNotNull(LocalDateTime startTime, LocalDateTime endTime) throws InvalidTimeReservationException {
        if (startTime == null || endTime == null) {
            throw new InvalidTimeReservationException(startTime, endTime);
        }
    }

    private static void validateDatesNotInPast(LocalDateTime startTime, LocalDateTime endTime) throws InvalidTimeReservationException {
        LocalDateTime now = LocalDateTime.now();
        if (startTime.isBefore(now)) {
            throw new InvalidTimeReservationException(startTime, endTime);
        }
        if (endTime.isBefore(now)) {
            throw new InvalidTimeReservationException(startTime, endTime);
        }
    }

    private static void validateStartNotAfterEnd(LocalDateTime startTime, LocalDateTime endTime) throws InvalidTimeReservationException {
        if (startTime.isAfter(endTime)) {
            throw new InvalidTimeReservationException(startTime, endTime);
        }
    }

    private static void validateDurationAtLeastOneHour(LocalDateTime startTime, LocalDateTime endTime) throws InvalidTimeReservationException {
        long hoursBetween = ChronoUnit.HOURS.between(startTime, endTime);
        if (hoursBetween < 1) {
            throw new InvalidTimeReservationException(startTime, endTime);
        }
    }
}