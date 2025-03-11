package org.example.coworking.infrastructure.mapper;

import org.example.coworking.controller.InputFormatException;
import org.example.coworking.infrastructure.controller.exception.CoworkingTypeNotFoundException;
import org.example.coworking.infrastructure.controller.exception.PriceFormatException;
import org.example.coworking.infrastructure.controller.validator.InputValidator;
import org.example.coworking.model.CoworkingType;
import org.example.coworking.model.Facility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.example.coworking.infrastructure.controller.CoworkingController.FACILITY_MENU;
import static org.example.coworking.infrastructure.logger.Log.TECHNICAL_LOGGER;
import static org.example.coworking.infrastructure.logger.Log.USER_OUTPUT_LOGGER;

public class CoworkingMapper {
    public double getPrice(String priceInput) throws PriceFormatException {
        return Double.parseDouble(priceInput);
    }

    public CoworkingType getCoworkingType(String coworkingTypeInput) throws InputFormatException, CoworkingTypeNotFoundException {
        if (!InputValidator.isNumber(coworkingTypeInput)) {
            throw new InputFormatException(coworkingTypeInput);
        }
        int coworkingTypeIndex = Integer.parseInt(coworkingTypeInput);

        if (!InputValidator.isCoworkingTypeInputValid(coworkingTypeIndex)) {
            throw new CoworkingTypeNotFoundException(coworkingTypeIndex);
        } else
            return CoworkingType.values()[coworkingTypeIndex];
    }

    public List<Facility> getFacility(String facilityInput) {
        boolean areChosen = false;
        List<Facility> selectedFacilities = new ArrayList<>();
        while (!areChosen) {
            USER_OUTPUT_LOGGER.info(FACILITY_MENU);

            String facilitiesIndexesOnOneLine = reader.readLine();
            if (facilitiesIndexesOnOneLine.matches("")) {
                areChosen = true;
            } else if (InputValidator.isFacilityStringFromUserValid(facilitiesIndexesOnOneLine)) {
                String[] facilitiesIndexes = facilitiesIndexesOnOneLine.split(",");
                Arrays.sort(facilitiesIndexes);
                facilitiesIndexes = Arrays.stream(facilitiesIndexes).distinct().toArray(String[]::new);
                for (String facilitiesIndex : facilitiesIndexes) {
                    int facilityIndex = Integer.parseInt(facilitiesIndex.trim());
                    if (facilityIndex >= 0 && facilityIndex < Facility.values().length) {
                        selectedFacilities.add(Facility.values()[facilityIndex]);
                        areChosen = true;
                    }
                }
            } else {
                USER_OUTPUT_LOGGER.warn("You entered wrong number(s): " + facilitiesIndexesOnOneLine + ". Try again:\n");
                TECHNICAL_LOGGER.warn("wrong number(s): " + facilitiesIndexesOnOneLine);
            }
        }
        return selectedFacilities;
    }
}
