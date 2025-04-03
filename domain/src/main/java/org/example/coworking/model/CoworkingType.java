package org.example.coworking.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.NoArgsConstructor;

@NoArgsConstructor(force = true)
public enum CoworkingType {
    OPEN_SPACE("Open Space"), PRIVATE_OFFICE("Private Office"), CO_LIVING("Co Living");

    private final String description;

    CoworkingType(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }
}