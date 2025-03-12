package org.example.coworking.model;

import lombok.*;


@Getter
@ToString
public enum Facility {
    PARKING(0), WIFI(1), KITCHEN(2), PRINTER(3), CONDITIONING(4);

    private final int code;

    Facility(int code) {
        this.code = code;
    }
}
