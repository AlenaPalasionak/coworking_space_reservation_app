package org.example.coworking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@Getter
public class ReservationPeriod {
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
}
