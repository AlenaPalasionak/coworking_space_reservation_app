package org.example.coworking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(force = true)
//@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "reservations")
public class Reservation implements Comparable<Reservation> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "start_time")

    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "end_time")
    private LocalDateTime endTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coworking_space_id", nullable = false)
    private CoworkingSpace coworkingSpace;

    public Reservation(User customer, LocalDateTime startTime, LocalDateTime endTime, CoworkingSpace coworkingSpace) {
        this.customer = customer;
        this.startTime = startTime;
        this.endTime = endTime;
        this.coworkingSpace = coworkingSpace;
    }

    public Reservation(Long id, User customer, LocalDateTime startTime, LocalDateTime endTime, CoworkingSpace coworkingSpace) {
        this.id = id;
        this.customer = customer;
        this.startTime = startTime;
        this.endTime = endTime;
        this.coworkingSpace = coworkingSpace;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", customer ID=" + getCustomer().getId() +
                ", startTime = " + startTime +
                ", endTime = " + endTime +
                ", coworkingSpace ID = " + getCoworkingSpace().getId() + "\n" +
                '}';
    }

    @Override
    public int compareTo(Reservation other) {
        return this.startTime.compareTo(other.startTime);
    }
}
