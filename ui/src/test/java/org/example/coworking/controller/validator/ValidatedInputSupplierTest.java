package org.example.coworking.controller.validator;

import org.example.coworking.controller.exception.InvalidInputException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidatedInputSupplierTest {
    @Mock
    private Consumer<String> consumerMock;
    @Mock
    private Supplier<String> supplierMock;
    @Mock
    private Predicate<String> validatorMock;
    @InjectMocks
    private ValidatedInputSupplier validatedInputSupplier;

    @Test
    void testValidInputReturnsCorrectValue() throws InvalidInputException {
        String expectedInput = "ValidInput";

        when(supplierMock.get()).thenReturn(expectedInput);
        when(validatorMock.test(expectedInput)).thenReturn(true);

        String actualInput = validatedInputSupplier.supplier("Enter input:");

        assertEquals(expectedInput, actualInput);
        verify(consumerMock).accept("Enter input:");
    }

    @Test
    void testInvalidInputThrowsException() {
        String invalidInput = "InvalidInput";

        when(supplierMock.get()).thenReturn(invalidInput);
        when(validatorMock.test(invalidInput)).thenReturn(false);

        assertThrows(InvalidInputException.class,
                () -> validatedInputSupplier.supplier("Enter input:"));
        verify(consumerMock).accept("Enter input:");
    }
}
