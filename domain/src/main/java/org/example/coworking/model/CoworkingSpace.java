package org.example.coworking.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(force = true)
@Entity
public class CoworkingSpace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;
    @Column(nullable = false)
    private double price;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CoworkingType coworkingType;

    @ManyToMany
    @JoinTable(
            name = "coworking_space_facilities",
            joinColumns = @JoinColumn(name = "coworking_space_id"),
            inverseJoinColumns = @JoinColumn(name = "facility_id")
    )
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