package org.example.coworking.service.validator;

import org.example.coworking.service.exception.InvalidTimeLogicException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimeLogicValidator {
    private static final long MIN_HOURS_DURATION = 1;

    public void validateReservation(LocalDateTime startTime, LocalDateTime endTime) throws InvalidTimeLogicException {
        validateNotNull(startTime, endTime);
        validateDatesNotInPast(startTime, endTime);
        validateStartNotAfterEnd(startTime, endTime);
        validateDurationAtLeastOneHour(startTime, endTime);
    }

    private void validateNotNull(LocalDateTime startTime, LocalDateTime endTime) throws InvalidTimeLogicException {
        if (startTime == null || endTime == null) {
            throw new InvalidTimeLogicException("StartTime and endTime are null. ");
        }
    }

    private void validateDatesNotInPast(LocalDateTime startTime, LocalDateTime endTime) throws InvalidTimeLogicException {
        LocalDateTime now = LocalDateTime.now();
        if (startTime.isBefore(now)) {
            throw new InvalidTimeLogicException("StartTime: *" + startTime + "* is already in the past");
        }
        if (endTime.isBefore(now)) {
            throw new InvalidTimeLogicException("EndTime: *" + endTime + "* is already in the past");
        }
    }

    private void validateStartNotAfterEnd(LocalDateTime startTime, LocalDateTime endTime) throws InvalidTimeLogicException {
        if (startTime.isAfter(endTime)) {
            throw new InvalidTimeLogicException("StartTime: *" + startTime + "*  is after endTime: *" + endTime + "*");
        }
    }

    private void validateDurationAtLeastOneHour(LocalDateTime startTime, LocalDateTime endTime) throws InvalidTimeLogicException {
        long hoursBetween = ChronoUnit.HOURS.between(startTime, endTime);
        if (hoursBetween < MIN_HOURS_DURATION) {
            throw new InvalidTimeLogicException("Reservation duration is less than an hour");
        }
    }
}