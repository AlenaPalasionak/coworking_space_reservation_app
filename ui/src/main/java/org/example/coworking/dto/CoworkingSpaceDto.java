package org.example.coworking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.example.coworking.entity.CoworkingType;
import org.example.coworking.entity.Facility;

import java.util.Set;

@Data
public class CoworkingSpaceDto {
    @NotNull(message = "Coworking Space ID must not be null")
    @PositiveOrZero(message = "Coworking Space ID must be positive or zero")
    private Long adminId;
    @NotNull(message = "Price must not be 0.0")
    @Positive(message = "Price must be positive or zero")
    private double price;
    @NotNull(message = "CoworkingType must not be null")
    private CoworkingType coworkingType;
    private Set<Facility> facilities;
}
