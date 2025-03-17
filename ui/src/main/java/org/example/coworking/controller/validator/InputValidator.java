package org.example.coworking.controller.validator;

import java.io.BufferedReader;
import java.io.IOException;

import static org.example.coworking.logger.Log.USER_OUTPUT_LOGGER;

/**
 * This class provides utility methods for input validation.
 * It offers functionality to create a validated input supplier based on a given pattern.
 */
public class InputValidator {

    /**
     * Creates a {@link ValidatedInputSupplier} that reads input from the provided {@link BufferedReader}
     * and validates the input using the specified regular expression pattern.
     * The input is considered valid if it matches the pattern.
     *
     * @param reader the {@link BufferedReader} from which input will be read
     * @param pattern the regular expression pattern that the input should match
     * @return a {@link ValidatedInputSupplier} that validates the input based on the provided pattern
     */
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
}
