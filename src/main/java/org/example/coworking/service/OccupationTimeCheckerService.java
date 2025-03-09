package org.example.coworking.service;

import org.example.coworking.model.CoworkingSpace;
import org.example.coworking.model.ReservationPeriod;
import org.example.coworking.service.exception.TimeOverlapException;

public class OccupationTimeCheckerService {
    public static boolean isTimeOverlapping(ReservationPeriod newPeriod, CoworkingSpace coworkingSpace) throws TimeOverlapException {
        boolean isOverlapping = coworkingSpace.getReservationsPeriods().stream()
                .anyMatch(existingPeriod -> isOverlapping(existingPeriod, newPeriod));

        if (isOverlapping) {
            throw new TimeOverlapException(newPeriod);
        }
        return false;
    }

    private static boolean isOverlapping(ReservationPeriod existing, ReservationPeriod newPeriod) {
        return !(newPeriod.getEndTime().isBefore(existing.getStartTime())
                || newPeriod.getStartTime().isAfter(existing.getEndTime()));
    }
}
