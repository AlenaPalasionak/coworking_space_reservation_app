package org.example.coworking.service;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class InputSupplierCreator<T, K> {

    private Consumer<String> consumer;
    private Supplier<String> supplier;
    private Function<String, T> function;

    public InputSupplierCreator(Consumer<String> consumer, Supplier<String> supplier, Function<String, T> function) {
        this.consumer = consumer;
        this.supplier = supplier;
        this.function = function;
    }

    public InputSupplierCreator(Consumer<String> consumer, Supplier<String> supplier) {
        this.consumer = consumer;
        this.supplier = supplier;
        this.function = s -> (T) s;
    }

    public Supplier<T> supplier(String message, Predicate<T> validator) {
        return () -> {
            while (true) {
                consumer.accept(message);
                try {
                    String userInput = supplier.get(); // Получаем ввод пользователя как строку
                    T value = function.apply(userInput); // Преобразуем строку в нужный тип
                    if (validator.test(value)) {
                        return value;
                    } else {
                        consumer.accept("Invalid input: " + userInput + ". Please try again.");
                    }
                } catch (Exception e) {
                    consumer.accept("Invalid input format: " + e.getMessage() + ". Please try again.");
                }
            }
        };
    }
}

