package org.example.coworking.controller.exception;

import jakarta.validation.ValidationException;
import org.example.coworking.repository.exception.EntityNotFoundException;
import org.example.coworking.service.exception.ForbiddenActionException;
import org.example.coworking.service.exception.ReservationTimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
    public ResponseEntity<?> handleValidationException(ValidationException exception, BindingResult result) {
        StringBuilder errorMessage = new StringBuilder();
        for (FieldError error : result.getFieldErrors()) {
            errorMessage.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("\n");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage.toString());
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
