package org.example.coworking.entity;

import lombok.NoArgsConstructor;

@NoArgsConstructor(force = true)
public enum CoworkingType {
    OPEN_SPACE(0), PRIVATE_OFFICE(1), CO_LIVING(2);
    private final int code;

    CoworkingType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}