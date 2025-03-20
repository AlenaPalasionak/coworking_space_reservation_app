package org.example.coworking.mapper;

import java.time.LocalDateTime;

/**
 * The ReservationMapper class is responsible for mapping input data, typically in string format,
 * to specific objects and values related to reservations, such as LocalDateTime and Long identifiers.
 * This class provides utility methods to convert string inputs into a `LocalDateTime` object
 * and to parse and validate identifiers as `Long` values.
 */
public class ReservationMapper {

    /**
     * Converts individual string inputs for year, month, day, hour, and minute into a corresponding
     * LocalDateTime object.
     *
     * @param yearInput the string representation of the year
     * @param monthInput the string representation of the month
     * @param dayInput the string representation of the day
     * @param hourInput the string representation of the hour
     * @param minuteInput the string representation of the minute
     * @return a LocalDateTime object representing the parsed date and time
     */
    public LocalDateTime getLocalDateTime(String yearInput, String monthInput, String dayInput,
                                          String hourInput, String minuteInput) {
        int year = Integer.parseInt(yearInput);
        int month = Integer.parseInt(monthInput);
        int day = Integer.parseInt(dayInput);
        int hour = Integer.parseInt(hourInput);
        int minute = Integer.parseInt(minuteInput);

        return LocalDateTime.of(year, month, day, hour, minute);
    }

    /**
     * Converts a string representing an ID into a corresponding Long value.
     *
     * @param idInput the string representation of the ID
     * @return the parsed Long value of the ID
     * @throws NumberFormatException if the idInput cannot be parsed as a valid Long
     */
    public Long getId(String idInput) {
        return Long.parseLong(idInput);
    }
}

