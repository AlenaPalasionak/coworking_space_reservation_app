package org.example.coworking.service.validator;

import org.example.coworking.service.exception.ReservationTimeException;
import org.example.coworking.service.exception.ServiceErrorCode;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class TimeLogicValidator {
    private static final long MIN_HOURS_DURATION = 1;

    public void validateReservation(LocalDateTime startTime, LocalDateTime endTime) throws ReservationTimeException {
        validateNotNull(startTime, endTime);
        validateDatesNotInPast(startTime, endTime);
        validateStartNotAfterEnd(startTime, endTime);
        validateDurationAtLeastOneHour(startTime, endTime);
    }

    private void validateNotNull(LocalDateTime startTime, LocalDateTime endTime) throws ReservationTimeException {
        if (startTime == null || endTime == null) {
            throw new ReservationTimeException("StartTime and endTime are null. ", ServiceErrorCode.INVALID_TIME_LOGIC);
        }
    }

    private void validateDatesNotInPast(LocalDateTime startTime, LocalDateTime endTime) throws ReservationTimeException {
        LocalDateTime now = LocalDateTime.now();
        if (startTime.isBefore(now)) {
            throw new ReservationTimeException(String.format("StartTime: *%s* is already in the past", startTime)
                    , ServiceErrorCode.INVALID_TIME_LOGIC);
        }
        if (endTime.isBefore(now)) {
            throw new ReservationTimeException(String.format("EndTime: *%s* is already in the past", endTime)
                    , ServiceErrorCode.INVALID_TIME_LOGIC);
        }
    }

    private void validateStartNotAfterEnd(LocalDateTime startTime, LocalDateTime endTime) throws ReservationTimeException {
        if (startTime.isAfter(endTime)) {
            throw new ReservationTimeException(String.format("StartTime: *%s*  is after endTime: *%s*", startTime, endTime)
                    , ServiceErrorCode.INVALID_TIME_LOGIC);
        }
    }

    private void validateDurationAtLeastOneHour(LocalDateTime startTime, LocalDateTime endTime) throws ReservationTimeException {
        long hoursBetween = ChronoUnit.HOURS.between(startTime, endTime);
        if (hoursBetween < MIN_HOURS_DURATION) {
            throw new ReservationTimeException("Reservation duration is less than an hour", ServiceErrorCode.INVALID_TIME_LOGIC);
        }
    }
}