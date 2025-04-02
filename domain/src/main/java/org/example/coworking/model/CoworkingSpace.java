package org.example.coworking.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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

    public CoworkingSpace(User admin, double price, CoworkingType coworkingType, List<Facility> facilities) {
        this.admin = admin;
        this.price = price;
        this.coworkingType = coworkingType;
        this.facilities = facilities;
    }

    @Override
    public String toString() {
        return "CoworkingSpace{" +
                "id=" + id +
                ", admin=" + admin +
                ", price=" + price +
                ", coworkingType=" + coworkingType +
                ", facilities=" + facilities + "\n" +
                '}' + "\n";
    }
}