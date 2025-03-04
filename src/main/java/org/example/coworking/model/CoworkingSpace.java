package org.example.coworking.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(force = true)
public class CoworkingSpace {

    @Setter
    private int id;

    @JsonProperty("price")
    private final double price;

    @JsonProperty("coworkingType")
    private final CoworkingType coworkingType;

    @JsonProperty("facilities")
    private final List<Facility> facilities;

    @JsonProperty("reservationsPeriods")
    private final List<ReservationPeriod> reservationsPeriods;

    public CoworkingSpace(double price, CoworkingType coworkingType, List<Facility> facilities) {
        this.price = price;
        this.coworkingType = coworkingType;
        this.facilities = facilities;
        this.reservationsPeriods = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "CoworkingSpace{" + "id=" + id + ", price=" + price + ", coworkingType=" + coworkingType + ", facilities=" + facilities + ", reservationsPeriods=" + reservationsPeriods + '}';
    }
}