package org.example.coworking.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(force = true)
public class CoworkingSpace {
    private Long id;
    private User admin;
    private double price;
    private CoworkingType coworkingType;
    private List<Facility> facilities;
    private Set<Reservation> reservations;

    public CoworkingSpace(User admin, double price, CoworkingType coworkingType, List<Facility> facilities) {
        this.admin = admin;
        this.price = price;
        this.coworkingType = coworkingType;
        this.facilities = facilities;
        this.reservations = new TreeSet<>();
    }

    @Override
    public String toString() {
        return "CoworkingSpace{" +
                "id=" + id +
                ", admin=" + admin +
                ", price=" + price +
                ", coworkingType=" + coworkingType +
                ", facilities=" + facilities + "\n" +
                (reservations != null ? reservations.stream()
                        .map(reservation -> reservation.getPeriod().toString())
                        .collect(Collectors.joining("\n")) : "Reservations set is empty") +
                '}' + "\n";
    }
}