package org.example.coworking.infrastructure.mapper;

import org.example.coworking.infrastructure.mapper.exception.CoworkingTypeIndexException;
import org.example.coworking.infrastructure.mapper.exception.FacilityIndexException;
import org.example.coworking.model.CoworkingSpace;
import org.example.coworking.model.CoworkingType;
import org.example.coworking.model.Facility;
import org.example.coworking.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CoworkingMapper {

    public CoworkingSpace getCoworkingSpace(User user, double price, CoworkingType coworkingType, List<Facility> facilities) {
        return new CoworkingSpace(user, price, coworkingType, facilities);
    }

    public double getPrice(String priceInput) {
        return Double.parseDouble(priceInput);
    }

    public CoworkingType getCoworkingType(String coworkingTypeInput) throws CoworkingTypeIndexException {
        int coworkingTypeIndex = Integer.parseInt(coworkingTypeInput);
        if (!areCoworkingTypeIndexNotOutOfBound(coworkingTypeIndex)) {
            throw new CoworkingTypeIndexException(coworkingTypeIndex);
        } else
            return CoworkingType.values()[coworkingTypeIndex];
    }

    public List<Facility> getFacility(String facilityIndexesInput) throws FacilityIndexException {
        String hasNoFacilities = "";
        boolean areChosen = false;
        List<Facility> facilities = new ArrayList<>();
        while (!areChosen) {
            if (facilityIndexesInput.matches(hasNoFacilities)) {
                areChosen = true;
            } else {
                String[] facilitiesIndexes = facilityIndexesInput.split(",");
                Arrays.sort(facilitiesIndexes);
                facilitiesIndexes = Arrays.stream(facilitiesIndexes).distinct().toArray(String[]::new);
                for (String index : facilitiesIndexes) {
                    int facilityIndex = Integer.parseInt(index.trim());
                    if (facilityIndex >= 0 && facilityIndex < Facility.values().length) {
                        facilities.add(Facility.values()[facilityIndex]);
                        areChosen = true;
                    } else {
                        throw new FacilityIndexException(facilityIndex);
                    }
                }
            }
        }
        return facilities;
    }

    private static boolean areCoworkingTypeIndexNotOutOfBound(int coworkingTypeIndex) {
        return coworkingTypeIndex >= 0 && coworkingTypeIndex < CoworkingType.values().length;
    }
}
