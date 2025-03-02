package org.example.coworking.infrastructure.util;

import org.example.coworking.model.CoworkingSpace;
import org.example.coworking.model.ReservationPeriod;
import org.example.coworking.service.exception.TimeOverlapException;

public class OccupationTimeChecker {
    public static boolean isTimeOverlapping(ReservationPeriod newPeriod, CoworkingSpace coworkingSpace) throws TimeOverlapException {
        for (ReservationPeriod existingPeriod : coworkingSpace.getReservationsPeriods()) {
            if (isOverlapping(existingPeriod, newPeriod)) {
                throw new TimeOverlapException("Reservation overlaps with an existing period: " + newPeriod);
            }
        }
        return false;
    }

    private static boolean isOverlapping(ReservationPeriod existing, ReservationPeriod newPeriod) {
        return !(newPeriod.getEndTime().isBefore(existing.getStartTime()) || newPeriod.getStartTime().isAfter(existing.getEndTime()));
    }
}
