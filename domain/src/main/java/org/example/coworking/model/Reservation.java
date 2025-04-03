package org.example.coworking.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(force = true)
@Entity
@Table(name = "reservations")
public class Reservation implements Comparable<Reservation> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User customer;
    @Embedded
    private ReservationPeriod period;
    @ManyToOne
    private CoworkingSpace coworkingSpace;

    public Reservation(User customer, ReservationPeriod period, CoworkingSpace coworkingSpace) {
        this.customer = customer;
        this.period = period;
        this.coworkingSpace = coworkingSpace;
    }

    public Reservation(Long id, User customer, ReservationPeriod period, CoworkingSpace coworkingSpace) {
        this.id = id;
        this.customer = customer;
        this.period = period;
        this.coworkingSpace = coworkingSpace;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", customer=" + customer +
                ", period=" + period + "\n" +
                ", coworkingSpace=" + coworkingSpace + "\n" +
                '}';
    }

    @Override
    public int compareTo(Reservation other) {
        return this.period.compareTo(other.period);
    }
}
