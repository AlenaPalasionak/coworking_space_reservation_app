package org.example.coworking.controller.exception;

import jakarta.validation.ValidationException;
import org.example.coworking.repository.exception.DataExcessException;
import org.example.coworking.repository.exception.EntityNotFoundException;
import org.example.coworking.service.exception.ForbiddenActionException;
import org.example.coworking.service.exception.ReservationTimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.DateTimeException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<String> handleInvalidInputException(InvalidInputException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Invalid input: " + exception.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Validation error: " + exception.getMessage());
    }

    @ExceptionHandler(ForbiddenActionException.class)
    public ResponseEntity<String> handleForbiddenActionException(ForbiddenActionException exception) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("Forbidden action: " + exception.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Entity not found: " + exception.getMessage());
    }
    @ExceptionHandler(DataExcessException.class)
    public ResponseEntity<String> handleDataExcessException(DataExcessException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Data excess error: " + exception.getMessage());
    }

    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<String> handleReservationTimeException(DateTimeException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Reservation date/time error: " + exception.getMessage());
    }

    @ExceptionHandler(ReservationTimeException.class)
    public ResponseEntity<String> handleReservationTimeException(ReservationTimeException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Date/time logic error: " + exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + exception.getMessage());
    }
}
