package org.example.coworking.infrastructure.mapper;

import org.example.coworking.infrastructure.dao.exception.CoworkingNotFoundException;
import org.example.coworking.model.CoworkingSpace;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.example.coworking.infrastructure.logger.Log.TECHNICAL_LOGGER;
import static org.example.coworking.infrastructure.logger.Log.USER_OUTPUT_LOGGER;

public class ReservationMapper {

    public LocalDateTime getLocalDateTime(String yearInput, String monthInput, String dayInput, String hourInput, String minuteInput) {
        int year = Integer.parseInt(yearInput);
        int month = Integer.parseInt(monthInput);
        int day = Integer.parseInt(dayInput);
        int hour = Integer.parseInt(hourInput);
        int minute = Integer.parseInt(minuteInput);

        return LocalDateTime.of(year, month, day, hour, minute);
    }

    private Optional<CoworkingSpace> getCoworkingSpaceFromUser(int coworkingId) {
        try {
            return coworkingService.getById(coworkingId);
        } catch (CoworkingNotFoundException e) {
            USER_OUTPUT_LOGGER.warn(e.getMessage());
            TECHNICAL_LOGGER.warn(e.getMessage());
            return Optional.empty();
        }
    }
}
