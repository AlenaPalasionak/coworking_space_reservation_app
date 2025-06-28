package org.example.coworking.service.validator;

import org.example.coworking.entity.Reservation;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OccupationTimeValidatorTest {

    private Reservation createReservation(LocalDateTime start, LocalDateTime end) {
        Reservation reservation = new Reservation();
        reservation.setStartTime(start);
        reservation.setEndTime(end);
        return reservation;
    }

    @Test
    void testOverlappingPeriodsReturnTrue() {
        Collection<Reservation> reservations = new TreeSet<>();
        reservations.add(createReservation(
                LocalDateTime.of(2025, 3, 20, 10, 0),
                LocalDateTime.of(2025, 3, 20, 12, 0)));

        LocalDateTime newStart = LocalDateTime.of(2025, 3, 20, 11, 0);
        LocalDateTime newEnd = LocalDateTime.of(2025, 3, 20, 13, 0);

        assertTrue(OccupationTimeValidator.isTimeOverlapping(newStart, newEnd, reservations));
    }

    @Test
    void testNonOverlappingPeriodsReturnFalse() {
        Collection<Reservation> reservations = new TreeSet<>();
        reservations.add(createReservation(
                LocalDateTime.of(2025, 3, 20, 10, 0),
                LocalDateTime.of(2025, 3, 20, 12, 0)));

        LocalDateTime newStart = LocalDateTime.of(2025, 3, 20, 12, 1);
        LocalDateTime newEnd = LocalDateTime.of(2025, 3, 20, 14, 0);

        assertFalse(OccupationTimeValidator.isTimeOverlapping(newStart, newEnd, reservations));
    }

    @Test
    void testExactlySamePeriodReturnsTrue() {
        Collection<Reservation> reservations = new TreeSet<>();
        reservations.add(createReservation(
                LocalDateTime.of(2025, 3, 20, 10, 0),
                LocalDateTime.of(2025, 3, 20, 12, 0)));

        LocalDateTime newStart = LocalDateTime.of(2025, 3, 20, 10, 0);
        LocalDateTime newEnd = LocalDateTime.of(2025, 3, 20, 12, 0);

        assertTrue(OccupationTimeValidator.isTimeOverlapping(newStart, newEnd, reservations));
    }

    @Test
    void testNewPeriodEndsBeforeExistingStartsReturnsFalse() {
        Collection<Reservation> reservations = new TreeSet<>();
        reservations.add(createReservation(
                LocalDateTime.of(2025, 3, 20, 10, 0),
                LocalDateTime.of(2025, 3, 20, 12, 0)));

        LocalDateTime newStart = LocalDateTime.of(2025, 3, 20, 8, 0);
        LocalDateTime newEnd = LocalDateTime.of(2025, 3, 20, 9, 59);

        assertFalse(OccupationTimeValidator.isTimeOverlapping(newStart, newEnd, reservations));
    }

    @Test
    void testNewPeriodStartsAfterExistingEndsReturnsFalse() {
        Collection<Reservation> reservations = new TreeSet<>();
        reservations.add(createReservation(
                LocalDateTime.of(2025, 3, 20, 10, 0),
                LocalDateTime.of(2025, 3, 20, 12, 0)));

        LocalDateTime newStart = LocalDateTime.of(2025, 3, 20, 12, 1);
        LocalDateTime newEnd = LocalDateTime.of(2025, 3, 20, 14, 0);

        assertFalse(OccupationTimeValidator.isTimeOverlapping(newStart, newEnd, reservations));
    }
}
