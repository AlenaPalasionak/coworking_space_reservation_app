package org.example.coworking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ReservationDto {
    @NotNull(message = "Customer ID must not be null")
    @PositiveOrZero(message = "Customer ID must be positive or zero")
    private Long customerId;

    @NotNull(message = "Start time must not be null")
    @FutureOrPresent(message = "Start time must be in the present or future")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;

    @NotNull(message = "End time must not be null")
    @Future(message = "End time must be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    @NotNull(message = "Coworking space ID must not be null")
    @PositiveOrZero(message = "Coworking space ID must be positive or zero")
    private Long coworkingSpaceId;
}
