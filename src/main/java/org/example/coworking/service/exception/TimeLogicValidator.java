package org.example.coworking.service.exception;

import org.example.coworking.service.util.InvalidTimeLogicException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimeLogicValidator {

    public static void validateReservation(LocalDateTime startTime, LocalDateTime endTime) throws InvalidTimeLogicException {
        validateNotNull(startTime, endTime);
        validateDatesNotInPast(startTime, endTime);
        validateStartNotAfterEnd(startTime, endTime);
        validateDurationAtLeastOneHour(startTime, endTime);
    }

    private static void validateNotNull(LocalDateTime startTime, LocalDateTime endTime) throws InvalidTimeLogicException {
        if (startTime == null || endTime == null) {
            throw new InvalidTimeLogicException(startTime, endTime);
        }
    }

    private static void validateDatesNotInPast(LocalDateTime startTime, LocalDateTime endTime) throws InvalidTimeLogicException {
        LocalDateTime now = LocalDateTime.now();
        if (startTime.isBefore(now)) {
            throw new InvalidTimeLogicException(startTime, endTime);
        }
        if (endTime.isBefore(now)) {
            throw new InvalidTimeLogicException(startTime, endTime);
        }
    }

    private static void validateStartNotAfterEnd(LocalDateTime startTime, LocalDateTime endTime) throws InvalidTimeLogicException {
        if (startTime.isAfter(endTime)) {
            throw new InvalidTimeLogicException(startTime, endTime);
        }
    }

    private static void validateDurationAtLeastOneHour(LocalDateTime startTime, LocalDateTime endTime) throws InvalidTimeLogicException {
        long hoursBetween = ChronoUnit.HOURS.between(startTime, endTime);
        if (hoursBetween < 1) {
            throw new InvalidTimeLogicException(startTime, endTime);
        }
    }
}