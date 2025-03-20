package org.example.coworking.service.validator;

import org.example.coworking.model.ReservationPeriod;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OccupationTimeValidatorTest {

    @Test
    void testOverlappingPeriodsReturnTrue() {
        TreeSet<ReservationPeriod> periods = new TreeSet<>();
        periods.add(new ReservationPeriod(LocalDateTime.of(2025, 3, 20, 10, 0),
                LocalDateTime.of(2025, 3, 20, 12, 0)));

        ReservationPeriod newPeriod = new ReservationPeriod(LocalDateTime.of(2025, 3, 20, 11, 0),
                LocalDateTime.of(2025, 3, 20, 13, 0));

        assertTrue(OccupationTimeValidator.isTimeOverlapping(newPeriod, periods));
    }

    @Test
    void testNonOverlappingPeriodsReturnFalse() {
        TreeSet<ReservationPeriod> periods = new TreeSet<>();
        periods.add(new ReservationPeriod(LocalDateTime.of(2025, 3, 20, 10, 0),
                LocalDateTime.of(2025, 3, 20, 12, 0)));

        ReservationPeriod newPeriod = new ReservationPeriod(LocalDateTime.of(2025, 3, 20, 12, 1),
                LocalDateTime.of(2025, 3, 20, 14, 0));

        assertFalse(OccupationTimeValidator.isTimeOverlapping(newPeriod, periods));
    }

    @Test
    void testExactlySamePeriodReturnsTrue() {
        TreeSet<ReservationPeriod> periods = new TreeSet<>();
        ReservationPeriod existingPeriod = new ReservationPeriod(LocalDateTime.of(2025, 3, 20, 10, 0),
                LocalDateTime.of(2025, 3, 20, 12, 0));
        periods.add(existingPeriod);

        ReservationPeriod newPeriod = new ReservationPeriod(LocalDateTime.of(2025, 3, 20, 10, 0),
                LocalDateTime.of(2025, 3, 20, 12, 0));

        assertTrue(OccupationTimeValidator.isTimeOverlapping(newPeriod, periods));
    }

    @Test
    void testNewPeriodEndsBeforeExistingStartsReturnsFalse() {
        TreeSet<ReservationPeriod> periods = new TreeSet<>();
        periods.add(new ReservationPeriod(LocalDateTime.of(2025, 3, 20, 10, 0),
                LocalDateTime.of(2025, 3, 20, 12, 0)));

        ReservationPeriod newPeriod = new ReservationPeriod(LocalDateTime.of(2025, 3, 20, 8, 0),
                LocalDateTime.of(2025, 3, 20, 9, 59));

        assertFalse(OccupationTimeValidator.isTimeOverlapping(newPeriod, periods));
    }

    @Test
    void testNewPeriodStartsAfterExistingEndsReturnsFalse() {
        TreeSet<ReservationPeriod> periods = new TreeSet<>();
        periods.add(new ReservationPeriod(LocalDateTime.of(2025, 3, 20, 10, 0),
                LocalDateTime.of(2025, 3, 20, 12, 0)));

        ReservationPeriod newPeriod = new ReservationPeriod(LocalDateTime.of(2025, 3, 20, 12, 1),
                LocalDateTime.of(2025, 3, 20, 14, 0));

        assertFalse(OccupationTimeValidator.isTimeOverlapping(newPeriod, periods));
    }
}
