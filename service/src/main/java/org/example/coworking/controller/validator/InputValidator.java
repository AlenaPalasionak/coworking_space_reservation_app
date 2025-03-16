package org.example.coworking.controller.validator;

import java.io.BufferedReader;
import java.io.IOException;

import static org.example.coworking.logger.Log.USER_OUTPUT_LOGGER;

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
}