package org.example.coworking.entity;

import org.example.coworking.entity.exception.EnumErrorCode;
import org.example.coworking.entity.exception.FacilityTypeIndexException;

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

    public static Facility fromCode(int code) throws FacilityTypeIndexException {
        for (Facility facility : Facility.values()) {
            if (facility.code == code) {
                return facility;
            }
        }
        throw new FacilityTypeIndexException(String.format("Code: %d is out of bound in enum Facility.",
                code), EnumErrorCode.INVALID_FACILITY_INDEX);
    }
}

