package org.example.coworking.model;

import lombok.NoArgsConstructor;
import org.example.coworking.model.exception.CoworkingTypeIndexException;
import org.example.coworking.model.exception.EnumErrorCode;

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

    public static CoworkingType fromCode(int code) throws CoworkingTypeIndexException{
        for (CoworkingType type : CoworkingType.values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new CoworkingTypeIndexException(String.format("Code: %d is out of bound in enum CoworkingType.",
                code), EnumErrorCode.INVALID_COWORKING_TYPE_INDEX);
    }
}