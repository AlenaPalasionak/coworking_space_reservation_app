package org.example.coworking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CoworkingType {
    OPEN_SPACE("Open space coworking"),
    PRIVATE_OFFICE( "Private Office"),
    CO_LIVING( "Coworking + Co-living");

    private final String description;
}
