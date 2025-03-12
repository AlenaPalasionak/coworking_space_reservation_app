package org.example.coworking.infrastructure.mapper;

import java.time.LocalDateTime;

public class ReservationMapper {

    public LocalDateTime getLocalDateTime(String yearInput, String monthInput, String dayInput, String hourInput, String minuteInput) {
        int year = Integer.parseInt(yearInput);
        int month = Integer.parseInt(monthInput);
        int day = Integer.parseInt(dayInput);
        int hour = Integer.parseInt(hourInput);
        int minute = Integer.parseInt(minuteInput);

        return LocalDateTime.of(year, month, day, hour, minute);
    }

    public int getId(String idInput) {
        return Integer.parseInt(idInput);
    }
}
