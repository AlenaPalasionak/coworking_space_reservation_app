package org.example.coworking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(force = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Embeddable
public class ReservationPeriod implements Comparable<ReservationPeriod> {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "start_time")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "end_time")
    private LocalDateTime endTime;

    public ReservationPeriod(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public int compareTo(ReservationPeriod other) {
        return this.startTime.compareTo(other.getStartTime());
    }
}
