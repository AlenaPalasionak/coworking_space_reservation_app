package org.example.coworking.service.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class InputSupplierCreator {
    private final Consumer<String> consumer;
    private final Supplier<String> supplier;

    public InputSupplierCreator(Consumer<String> consumer, Supplier<String> supplier) {
        this.consumer = consumer;
        this.supplier = supplier;
    }

    public String supplier(String message) {
        consumer.accept(message);
        return supplier.get();
    }
}