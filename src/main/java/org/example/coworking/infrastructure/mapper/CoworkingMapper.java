package org.example.coworking.infrastructure.mapper;

import org.example.coworking.infrastructure.mapper.exception.CoworkingTypeIndexException;
import org.example.coworking.infrastructure.mapper.exception.FacilityIndexException;
import org.example.coworking.infrastructure.mapper.exception.MapperErrorCode;
import org.example.coworking.model.CoworkingType;
import org.example.coworking.model.Facility;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CoworkingMapper {

    public double getPrice(String priceInput) {
        return Double.parseDouble(priceInput);
    }

    public CoworkingType getCoworkingType(String coworkingTypeInput) throws CoworkingTypeIndexException {
        int coworkingTypeIndex = Integer.parseInt(coworkingTypeInput);
        if (isIndexOutOfBound(coworkingTypeIndex, CoworkingType.class)) {
            throw new CoworkingTypeIndexException("Index: " + coworkingTypeIndex + " is out of bound in enum CoworkingType. ");
        } else
            return CoworkingType.values()[coworkingTypeIndex];
    }

    public List<Facility> getFacility(String facilityIndexesInput) throws FacilityIndexException {
        if (facilityIndexesInput.isBlank()) {
            return List.of();
        }

        return Arrays.stream(facilityIndexesInput.split(","))
                .map(String::trim)
                .distinct()
                .sorted()
                .map(Integer::parseInt)
                .map(index -> {
                    if (isIndexOutOfBound(index, Facility.class)) {
                        throw new FacilityIndexException("Index: " + index + " is out of bound in enum Facility. "
                        , MapperErrorCode.INVALID_FACILITY_INDEX);
                    }
                    return Facility.values()[index];
                })
                .collect(Collectors.toList());
    }

    private static <T extends Enum<T>> boolean isIndexOutOfBound(int enumIndex, Class<T> enumClass) {
        return enumIndex < 0 || enumIndex >= enumClass.getEnumConstants().length;
    }
}
