package org.example.coworking.infrastructure.controller.validator;

import org.example.coworking.infrastructure.controller.exception.PriceFormatException;
import org.example.coworking.model.CoworkingType;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Pattern;

import static org.example.coworking.infrastructure.logger.Log.TECHNICAL_LOGGER;
import static org.example.coworking.infrastructure.logger.Log.USER_OUTPUT_LOGGER;

public class InputValidator {

    public static boolean isFacilityStringFromUserValid(String input) {
        return Pattern.compile("^\\s*\\d+(\\s*,\\s*\\d+)*\\s*$").matcher(input).find();
    }

    public static boolean isNumber(String input) {
        return input.matches("\\d+");
    }

    public static boolean isPriceValid(String priceInput) throws PriceFormatException {
        if (priceInput == null || priceInput.trim().isEmpty() || !priceInput.matches("\\d+(\\.\\d+)?")) {
            throw new PriceFormatException(priceInput);
        }

        return true;
    }

    public static boolean isCoworkingTypeInputValid(int coworkingTypeIndex) {
        return coworkingTypeIndex > 0 || coworkingTypeIndex < CoworkingType.values().length;
    }

    public static int convertInputToInt(BufferedReader reader, String message) throws IOException {
        while (true) {
            try {
                USER_OUTPUT_LOGGER.info(message);

                return Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                USER_OUTPUT_LOGGER.error("Invalid input: not a number. Please try again.");
                TECHNICAL_LOGGER.error("Invalid input: not a number. Please try again.");
            }
        }
    }

    public static int getIntInputInRange(BufferedReader reader, String message, int min, int max) throws IOException {
        while (true) {
            int value = InputValidator.convertInputToInt(reader, message);
            if (value >= min && value <= max) {
                return value;
            } else {
                USER_OUTPUT_LOGGER.error("Invalid input: must be between {} and {}. Please try again.", min, max);
            }
        }
    }
}