package org.example.coworking.infrastructure.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Pattern;

import static org.example.coworking.infrastructure.logger.Log.USER_OUTPUT_LOGGER;
import static org.example.coworking.infrastructure.logger.Log.TECHNICAL_LOGGER;

public class InputValidator {

    public static boolean isFacilityStringFromUserValid(String input) {
        return Pattern.compile("^\\s*\\d+(\\s*,\\s*\\d+)*\\s*$").matcher(input).find();
    }

    public static int getInput(BufferedReader reader, String message) throws IOException {
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
}
