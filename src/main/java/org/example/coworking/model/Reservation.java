package org.example.coworking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Reservation {
    private int id;
    private Customer customer;
    private int coworkingId;
    private ReservationPeriod period;
    Coworking coworking;

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", customer=" + customer +
                ", coworkingId=" + coworkingId +
                ", period=" + period +
                '}';
    }
}
