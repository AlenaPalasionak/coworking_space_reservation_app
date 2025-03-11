package org.example.coworking.infrastructure.controller.validator;

import java.io.BufferedReader;
import java.io.IOException;

import static org.example.coworking.infrastructure.logger.Log.TECHNICAL_LOGGER;
import static org.example.coworking.infrastructure.logger.Log.USER_OUTPUT_LOGGER;

public class InputValidator {

    public static ValidatedInputSupplier getInputSupplier(BufferedReader reader, String pattern) {
        return new ValidatedInputSupplier(
                USER_OUTPUT_LOGGER::info,
                () -> {
                    try {
                        return reader.readLine().trim();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                },
                input -> input.matches(pattern)
        );
    }

//    public static boolean isFacilityStringFromUserValid(String input) {
//        return input.matches("^\\s*\\d+(\\s*,\\s*\\d+)*\\s*$");
//    }

    public static boolean isNumber(String input) {
        return input.matches("\\d+");
    }

//    public static boolean isPriceInputValid(String priceInput) {
//        return priceInput != null && priceInput.trim().matches("\\d+(\\.\\d+)?");
//    }

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