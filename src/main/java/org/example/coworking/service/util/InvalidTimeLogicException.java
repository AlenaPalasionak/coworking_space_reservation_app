package org.example.coworking.service.util;

import java.time.LocalDateTime;

public class InvalidTimeLogicException extends Throwable {
    public InvalidTimeLogicException(LocalDateTime startTime, LocalDateTime endTime) {
        super("Error in startTime or endTime: \n" + " Start time = " + startTime + ".\n End time = " + endTime);
    }
}
