package org.example.coworking.controller.validator;

import org.example.coworking.controller.exception.ControllerErrorCode;
import org.example.coworking.controller.exception.InvalidInputException;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * This class provides functionality for supplying and validating input.
 * It allows input to be retrieved, validated against a specified condition,
 * and handled in case of invalid input.
 */
public class ValidatedInputSupplier {

    private final Consumer<String> consumer;
    private final Supplier<String> supplier;
    private final Predicate<String> validator;

    /**
     * Constructs a new instance of {@link ValidatedInputSupplier} with the specified consumer,
     * supplier, and validator.
     *
     * @param consumer  a {@link Consumer} to handle the message to be displayed before input
     * @param supplier  a {@link Supplier} that provides the user input
     * @param validator a {@link Predicate} that validates the input
     */
    public ValidatedInputSupplier(Consumer<String> consumer, Supplier<String> supplier, Predicate<String> validator) {
        this.consumer = consumer;
        this.supplier = supplier;
        this.validator = validator;
    }

    /**
     * Retrieves input from the user, displays the provided message, validates the input,
     * and throws an exception if the input is invalid.
     *
     * @param message the message to be displayed before the input prompt
     * @return the valid input entered by the user
     * @throws InvalidInputException if the input does not meet the validation criteria
     */
    public String supplier(String message) throws InvalidInputException {
        consumer.accept(message);
        String input = supplier.get();
        if (!validator.test(input)) {
            throw new InvalidInputException("Wrong input: " + input, ControllerErrorCode.INVALID_INPUT);
        }
        return input;
    }
}

