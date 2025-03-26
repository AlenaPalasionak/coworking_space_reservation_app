package org.example.coworking.model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(force = true)
public class Reservation {

    private Long id;
    private final User customer;
    private final ReservationPeriod period;
    private final CoworkingSpace coworkingSpace;

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
                //   "id=" + id +
                ", customer=" + customer +
                ", period=" + period + "\n" +
                ", coworkingSpace=" + coworkingSpace + "\n" +
                '}';
    }
}
