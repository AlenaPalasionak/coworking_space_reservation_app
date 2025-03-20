package org.example.coworking.service.validator;

import org.example.coworking.model.CoworkingSpace;
import org.example.coworking.model.ReservationPeriod;

public class OccupationTimeValidator {
    public static boolean isTimeOverlapping(ReservationPeriod newPeriod, CoworkingSpace coworkingSpace) {
        return coworkingSpace.getReservationsPeriods().stream()
                .anyMatch(existingPeriod -> isOverlapping(existingPeriod, newPeriod));
    }

    private static boolean isOverlapping(ReservationPeriod existing, ReservationPeriod newPeriod) {
        return !(newPeriod.getEndTime().isBefore(existing.getStartTime())
                || newPeriod.getStartTime().isAfter(existing.getEndTime()));
    }
}
