package org.example.coworking.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;


@ToString
public enum Facility {
    PARKING("parking"), WIFI("wifi"), KITCHEN("kitchen"), PRINTER("printer"), CONDITIONING("conditioning");
    private final String description;

    Facility(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }
}