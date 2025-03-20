package org.example.coworking.service.validator;

import org.example.coworking.service.exception.ReservationTimeException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TimeLogicValidatorTest {

    private final TimeLogicValidator validator = new TimeLogicValidator();

    @Test
    void testValidReservationDoesNotThrowException() {
        LocalDateTime startTime = LocalDateTime.now().plusHours(3);
        LocalDateTime endTime = LocalDateTime.now().plusHours(5);

        assertDoesNotThrow(() -> validator.validateReservation(startTime, endTime));
    }

    @Test
    void testNullStartTimeThrowsException() {
        LocalDateTime endTime = LocalDateTime.now().plusHours(2);

        assertThrows(ReservationTimeException.class,
                () -> validator.validateReservation(null, endTime));
    }

    @Test
    void testNullEndTimeThrowsException() {
        LocalDateTime startTime = LocalDateTime.now().plusHours(2);

        assertThrows(ReservationTimeException.class,
                () -> validator.validateReservation(startTime, null));
    }

    @Test
    void testStartTimeInPastThrowsException() {
        LocalDateTime startTime = LocalDateTime.now().minusHours(2);
        LocalDateTime endTime = LocalDateTime.now().plusHours(2);

        assertThrows(ReservationTimeException.class,
                () -> validator.validateReservation(startTime, endTime));
    }

    @Test
    void testEndTimeInPastThrowsException() {
        LocalDateTime startTime = LocalDateTime.now().plusHours(2);
        LocalDateTime endTime = LocalDateTime.now().minusHours(2);

        assertThrows(ReservationTimeException.class,
                () -> validator.validateReservation(startTime, endTime));
    }

    @Test
    void testStartTimeAfterEndTimeThrowsException() {
        LocalDateTime startTime = LocalDateTime.now().plusHours(5);
        LocalDateTime endTime = LocalDateTime.now().plusHours(3);

        assertThrows(ReservationTimeException.class,
                () -> validator.validateReservation(startTime, endTime));
    }

    @Test
    void testDurationLessThanOneHourThrowsException() {
        LocalDateTime startTime = LocalDateTime.now().plusHours(2);
        LocalDateTime endTime = startTime.plusMinutes(30);

        assertThrows(ReservationTimeException.class,
                () -> validator.validateReservation(startTime, endTime));
    }
}