package org.example.coworking.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.TreeSet;

@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor(force = true)
public class CoworkingSpace {

    @Setter
    private int id;

    @JsonProperty("admin")
    private final User admin;

    @JsonProperty("price")
    private final double price;

    @JsonProperty("coworkingType")
    private final CoworkingType coworkingType;

    @JsonProperty("facilities")
    private final List<Facility> facilities;

    @JsonProperty("reservationsPeriods")
    private final TreeSet<ReservationPeriod> reservationsPeriods;

    public CoworkingSpace(User admin, double price, CoworkingType coworkingType, List<Facility> facilities) {
        this.admin = admin;
        this.price = price;
        this.coworkingType = coworkingType;
        this.facilities = facilities;
        this.reservationsPeriods = new TreeSet<>();
    }
}