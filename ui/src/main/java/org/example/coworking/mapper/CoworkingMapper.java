package org.example.coworking.mapper;

import org.example.coworking.model.exception.CoworkingTypeIndexException;
import org.example.coworking.model.exception.FacilityTypeIndexException;
import org.example.coworking.model.exception.EnumErrorCode;
import org.example.coworking.model.CoworkingType;
import org.example.coworking.model.Facility;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The CoworkingMapper class is responsible for converting input data (such as strings) into
 * corresponding values of various enumerations and data types related to coworking spaces.
 */
public class CoworkingMapper {

    /**
     * Converts a string representing a price into a double.
     *
     * @param priceInput the string representation of the price
     * @return the parsed price as a double
     * @throws NumberFormatException if the priceInput cannot be parsed as a valid double
     */
    public double getPrice(String priceInput) {
        return Double.parseDouble(priceInput);
    }

    /**
     * Converts a string representing a coworking type index into the corresponding enum value of CoworkingType.
     * Throws a CoworkingTypeIndexException if the index is invalid.
     *
     * @param coworkingTypeInput the string representing the coworking type index
     * @return the corresponding CoworkingType enum value
     * @throws CoworkingTypeIndexException if the coworking type index is invalid
     */
    public CoworkingType getCoworkingType(String coworkingTypeInput) throws CoworkingTypeIndexException {
        try {
            int coworkingTypeCode = Integer.parseInt(coworkingTypeInput);
            return CoworkingType.fromCode(coworkingTypeCode);
        } catch (NumberFormatException e) {
            throw new CoworkingTypeIndexException(
                    String.format("Invalid input format for coworking type index: '%s'", coworkingTypeInput),
                    EnumErrorCode.INVALID_COWORKING_TYPE_INDEX
            );
        }
    }

    /**
     * Converts a comma-separated string of facility indexes into a list of corresponding Facility enum values.
     * The string is split, trimmed, and parsed, and any invalid index will result in a FacilityIndexException.
     * If the input string is blank, an empty list is returned.
     *
     * @param facilityCodesInput the string of comma-separated facility indexes
     * @return a list of corresponding Facility enum values
     * @throws FacilityTypeIndexException if any facility index is invalid or out of bounds
     */
    public Set<Facility> getFacility(String facilityCodesInput) throws FacilityTypeIndexException {
        if (facilityCodesInput.isBlank()) {
            return Set.of();
        }

        return Arrays.stream(facilityCodesInput.split(","))
                .map(String::trim)
                .distinct()
                .sorted()
                .map(Integer::parseInt)
                .map(Facility::fromCode)
                .collect(Collectors.toSet());
    }
}
