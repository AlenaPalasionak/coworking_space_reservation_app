package org.example.coworking.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Coworking {
    private final int id;
    private final double price;
    private final CoworkingType coworkingType;
    private final Facility facility;
    private final List<ReservationPeriod> reservationsPeriods;

    public Coworking(int id, double price, CoworkingType coworkingType, Facility facility) {
        this.id = id;
        this.price = price;
        this.coworkingType = coworkingType;
        this.facility = facility;
        this.reservationsPeriods = new ArrayList<>();
    }

    @Override
    public String toString() {
        return " Coworking { " +
                "id=" + id +
                ", price=" + price +
                ", coworkingType=" + coworkingType +
                ", facility=" + facility +
                ", reservationsPeriods=" + reservationsPeriods +
                "}\n";
    }
}
