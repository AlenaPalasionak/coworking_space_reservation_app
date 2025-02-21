package org.example.coworking.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class ReservationPeriod {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
}
