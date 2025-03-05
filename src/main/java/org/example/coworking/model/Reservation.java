package org.example.coworking.model;

import lombok.*;

@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor(force = true)
public class Reservation {
    @Setter
    private int id;
    private final User customer;
    private final ReservationPeriod period;
    private final CoworkingSpace coworkingSpace;

    public Reservation(User customer, ReservationPeriod period, CoworkingSpace coworkingSpace) {
        this.customer = customer;
        this.period = period;
        this.coworkingSpace = coworkingSpace;
    }
}
