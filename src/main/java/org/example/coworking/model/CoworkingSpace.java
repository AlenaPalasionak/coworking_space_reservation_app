package org.example.coworking.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CoworkingSpace {
    @Setter
    private int id;
    private final double price;
    private final CoworkingType coworkingType;
    private final Facility facility;
    private final List<ReservationPeriod> reservationsPeriods;

    public CoworkingSpace(double price, CoworkingType coworkingType, Facility facility) {
        this.price = price;
        this.coworkingType = coworkingType;
        this.facility = facility;
        this.reservationsPeriods = new ArrayList<>();
    }

    @Override
    public String toString() {
        return " CoworkingSpace { " +
                "id=" + id +
                ", price=" + price +
                ", coworkingType=" + coworkingType +
                ", facility=" + facility +
                ", reservationsPeriods=" + reservationsPeriods +
                "}\n";
    }
}
