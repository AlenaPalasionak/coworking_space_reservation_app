package org.example.coworking.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(force = true)
@Entity
@Table(name = "coworking_spaces")
public class CoworkingSpace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;
    @Column(nullable = false)
    private double price;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private CoworkingType coworkingType;
    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "coworking_space_facilities",
            joinColumns = @JoinColumn(name = "coworking_space_id"),
            inverseJoinColumns = @JoinColumn(name = "facility_id")
    )
    private Set<Facility> facilities;

    public CoworkingSpace(User admin, double price, CoworkingType coworkingType, Set<Facility> facilities) {
        this.admin = admin;
        this.price = price;
        this.coworkingType = coworkingType;
        this.facilities = facilities;
    }

    @Override
    public String toString() {
        return "CoworkingSpace{" +
                "id=" + id +
                ", admin ID=" + getAdmin().getId() +
                ", price=" + price +
                ", coworkingType=" + coworkingType +
                ", facilities=" + facilities + "\n" +
                '}' + "\n";
    }
}