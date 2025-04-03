package org.example.coworking.model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(force = true)
public class Reservation implements Comparable<Reservation>{
    private Long id;
    private User customer;
    private ReservationPeriod period;
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
