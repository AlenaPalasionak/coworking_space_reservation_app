package org.example.coworking.service.validator;

import org.example.coworking.model.Reservation;

import java.time.LocalDateTime;
import java.util.Collection;

public class OccupationTimeValidator {

    public static boolean isTimeOverlapping(LocalDateTime newStart, LocalDateTime newEnd, Collection<Reservation> reservations) {
        return reservations.stream()
                .anyMatch(existing -> isOverlapping(existing, newStart, newEnd));
    }

    private static boolean isOverlapping(Reservation existing, LocalDateTime newStart, LocalDateTime newEnd) {
        LocalDateTime existingStart = existing.getStartTime();
        LocalDateTime existingEnd = existing.getEndTime();

        return !(newEnd.isBefore(existingStart) || newStart.isAfter(existingEnd));
    }
}
