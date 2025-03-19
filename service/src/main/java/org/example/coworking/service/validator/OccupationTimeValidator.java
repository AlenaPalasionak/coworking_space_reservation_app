package org.example.coworking.service.validator;

import org.example.coworking.model.ReservationPeriod;

import java.util.TreeSet;

public class OccupationTimeValidator {
    public static boolean isTimeOverlapping(ReservationPeriod newPeriod, TreeSet<ReservationPeriod> periods) {
        return periods.stream()
                .anyMatch(existingPeriod -> isOverlapping(existingPeriod, newPeriod));
    }

    private static boolean isOverlapping(ReservationPeriod existing, ReservationPeriod newPeriod) {
        return !(newPeriod.getEndTime().isBefore(existing.getStartTime())
                || newPeriod.getStartTime().isAfter(existing.getEndTime()));
    }
}
