package org.example.coworking.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class ReservationPeriod {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
