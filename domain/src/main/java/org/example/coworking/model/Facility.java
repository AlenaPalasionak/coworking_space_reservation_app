package org.example.coworking.model;

import org.example.coworking.model.exception.EnumErrorCode;
import org.example.coworking.model.exception.FacilityTypeIndexException;

public enum Facility {
    PARKING(0),
    WIFI(1),
    KITCHEN(2),
    PRINTER(3),
    CONDITIONING(4);

    private final int code;

    Facility(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static Facility fromCode(int code) {
        for (Facility facility : Facility.values()) {
            if (facility.code == code) {
                return facility;
            }
        }
        throw new FacilityTypeIndexException(String.format("Index: %d is out of bound in enum FacilityType.",
                code), EnumErrorCode.INVALID_FACILITY_INDEX);
    }
}

