package org.example.coworking.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "facilities")
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private FacilityType type;

    public Facility(FacilityType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Facility{type=" + type + "}";
    }
}

