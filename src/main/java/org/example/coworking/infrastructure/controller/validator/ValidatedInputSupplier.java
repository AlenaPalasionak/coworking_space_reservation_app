package org.example.coworking.infrastructure.controller.validator;

import org.example.coworking.infrastructure.controller.exception.InvalidInputException;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ValidatedInputSupplier {
    private final Consumer<String> consumer;
    private final Supplier<String> supplier;
    private final Predicate<String> validator;

    public ValidatedInputSupplier(Consumer<String> consumer, Supplier<String> supplier, Predicate<String> validator) {
        this.consumer = consumer;
        this.supplier = supplier;
        this.validator = validator;
    }

    public String supplier(String message) throws InvalidInputException {
        consumer.accept(message);
        String input = supplier.get();
        if (!validator.test(input)) {
            throw new InvalidInputException(input);
        }
        return input;
    }
}
