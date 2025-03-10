package org.example.coworking.infrastructure.controller;

import org.example.coworking.infrastructure.dao.exception.CoworkingNotFoundException;
import org.example.coworking.infrastructure.dao.exception.ReservationNotFoundException;
import org.example.coworking.infrastructure.util.InputValidator;
import org.example.coworking.infrastructure.util.exception.InvalidTimeReservationException;
import org.example.coworking.model.CoworkingSpace;
import org.example.coworking.model.Reservation;
import org.example.coworking.model.ReservationPeriod;
import org.example.coworking.model.User;
import org.example.coworking.service.CoworkingService;
import org.example.coworking.service.ReservationService;
import org.example.coworking.service.exception.ForbiddenActionException;
import org.example.coworking.service.exception.TimeOverlapException;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.example.coworking.infrastructure.logger.Log.USER_OUTPUT_LOGGER;
import static org.example.coworking.infrastructure.logger.Log.TECHNICAL_LOGGER;

public class ReservationController {
    private final CoworkingService coworkingService;
    private final ReservationService reservationService;

    public ReservationController(CoworkingService coworkingService, ReservationService reservationService) {
        this.coworkingService = coworkingService;
        this.reservationService = reservationService;
    }

    public void add(BufferedReader reader, User customer) throws IOException {
        boolean isFree = false;
        boolean isFound = false;
        List<CoworkingSpace> coworkingSpaces = coworkingService.getAllByUser(customer);
        USER_OUTPUT_LOGGER.info("Coworking spaces list:\n");
        coworkingSpaces.forEach(space -> USER_OUTPUT_LOGGER.info(space.toString()));

        while (!isFree && !isFound) {
            int coworkingId = getCoworkingIdFromUser(reader);
            LocalDateTime startTime = getDateTimeFromUser(reader, "start");
            LocalDateTime endTime = getDateTimeFromUser(reader, "end");

            ReservationPeriod period = new ReservationPeriod(startTime, endTime);

            Optional<CoworkingSpace> possibleCoworkingSpace = getCoworkingSpaceFromUser(coworkingId);
            if (possibleCoworkingSpace.isPresent()) {
                isFound = true;
                try {
                    CoworkingSpace coworkingSpace = possibleCoworkingSpace.get();
                    reservationService.add(customer, coworkingSpace, period);
                    USER_OUTPUT_LOGGER.info("You've just made a reservation:\n" + coworkingSpace + ", " + period);
                    isFree = true;
                } catch (TimeOverlapException e) {
                    USER_OUTPUT_LOGGER.warn("Choose other time. The coworking space is unavailable at this time\n");
                    TECHNICAL_LOGGER.warn(e.getMessage());
                } catch (InvalidTimeReservationException e) {
                    USER_OUTPUT_LOGGER.warn("You entered invalid date.\nTry again\n");
                    TECHNICAL_LOGGER.warn(e.getMessage());
                }
            }
        }
    }

    public void getAllReservations(User customer) {
        List<Reservation> reservations = reservationService.getAllByUser(customer);
        if (reservations.isEmpty()) {
            USER_OUTPUT_LOGGER.warn("Reservation list is empty\n");
            TECHNICAL_LOGGER.warn("Reservation list is empty\n");
        } else {
            USER_OUTPUT_LOGGER.info("Your reservation(s):\n");
            reservations.forEach(reservation -> USER_OUTPUT_LOGGER.info(reservation));
        }
    }

    public void delete(BufferedReader reader, User customer) throws IOException {
        List<Reservation> reservationsByCustomer = reservationService.getAllByUser(customer);
        USER_OUTPUT_LOGGER.info("Your reservations:\n");
        if (reservationsByCustomer.isEmpty()) {
            USER_OUTPUT_LOGGER.warn("Reservation list is empty:\n");
            TECHNICAL_LOGGER.warn("Reservation list is empty:\n");
        } else {
            reservationsByCustomer.forEach(reservation -> USER_OUTPUT_LOGGER.info(reservation.toString()));
            int reservationId = InputValidator.getIntInput(reader, "\nType reservation Id you want to cancel");
            Optional<Reservation> possibleReservation = Optional.empty();
            try {
                possibleReservation = reservationService.getById(reservationId);
            } catch (ReservationNotFoundException e) {
                USER_OUTPUT_LOGGER.warn("Reservation with Id " + reservationId + " is absent\n");
                TECHNICAL_LOGGER.warn("Reservation with Id " + reservationId + " is absent\n");
            }
            if (possibleReservation.isPresent()) {
                Reservation reservation = possibleReservation.get();

                try {
                    reservationService.delete(customer, reservation);
                } catch (ForbiddenActionException e) {
                    USER_OUTPUT_LOGGER.warn(e.getMessage() + "Reservation with id: " + reservationId + " belongs to another user");
                    TECHNICAL_LOGGER.warn(e.getMessage() + "Reservation with id: " + reservationId + " belongs to another user");
                } catch (ReservationNotFoundException e) {
                    USER_OUTPUT_LOGGER.warn(e.getMessage());
                    TECHNICAL_LOGGER.warn(e.getMessage() + "\n");
                }
                USER_OUTPUT_LOGGER.info("Reservation with Id " + reservationId + " is canceled\n");
            }
        }
    }

    public void load() {
        reservationService.load();
    }

    public void save() {
        reservationService.save();
    }

    private int getCoworkingIdFromUser(BufferedReader reader) throws IOException {
        USER_OUTPUT_LOGGER.info("Choose a CoworkingSpace and type its id to book it:\n");
        return Integer.parseInt(reader.readLine());
    }

    private LocalDateTime getDateTimeFromUser(BufferedReader reader, String timeType) throws IOException {
        while (true) {
            try {
                int year = InputValidator.getIntInput(reader, "Type a year. Format: yyyy");
                int month = getIntInputInRange(reader, "Type a month. Format: m or mm", 1, 12);
                int day = getIntInputInRange(reader, "Type a day. Format: d or dd", 1, 31); // Будет уточняться
                int hour = getIntInputInRange(reader, "Type " + timeType + " hour. Format: h or hh", 0, 23);
                int minute = getIntInputInRange(reader, "Type " + timeType + " minute. Format: m or mm", 0, 59);

                return LocalDateTime.of(year, month, day, hour, minute);
            } catch (DateTimeException e) {
                USER_OUTPUT_LOGGER.error("Invalid date or time value: {}. Please try again.", e.getMessage());
            }
        }
    }

    private int getIntInputInRange(BufferedReader reader, String message, int min, int max) throws IOException {
        while (true) {
            int value = InputValidator.getIntInput(reader, message);
            if (value >= min && value <= max) {
                return value;
            } else {
                USER_OUTPUT_LOGGER.error("Invalid input: must be between {} and {}. Please try again.", min, max);
            }
        }
    }

    private Optional<CoworkingSpace> getCoworkingSpaceFromUser(int coworkingId) {
        try {
            return coworkingService.getById(coworkingId);
        } catch (CoworkingNotFoundException e) {
            USER_OUTPUT_LOGGER.warn(e.getMessage());
            TECHNICAL_LOGGER.warn(e.getMessage());
            return Optional.empty();
        }
    }
}