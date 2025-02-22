package org.example.coworking.model;

import lombok.Getter;

@Getter
public enum CoworkingType {
    OPEN_SPACE("Open space coworking"),
    PRIVATE_OFFICE( "Private Office"),
    CO_LIVING( "Coworking + Co-living");

    private final String description;

    CoworkingType(String description) {
        this.description = description;
    }
}
