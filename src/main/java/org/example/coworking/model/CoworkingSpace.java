package org.example.coworking.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode
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

    @Override
    public String toString() {
        return "CoworkingSpace{" +
                "id=" + id +
                ", admin=" + admin +
                ", price=" + price + "\n" +
                ", coworkingType=" + coworkingType +
                ", facilities=" + facilities + "\n" +
                (reservationsPeriods != null ? reservationsPeriods.stream()
                        .map(ReservationPeriod::toString)
                        .collect(Collectors.joining("\n")) : "No reservations available") + "\n" +
                '}';
    }
}