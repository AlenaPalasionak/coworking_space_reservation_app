package org.example.coworking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Reservation {
    private int id;
    private Customer customer;
    private CoworkingSpace space;
    private ReservationPeriod period;
}
