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

import static org.example.coworking.infrastructure.logger.Log.CONSOLE_LOGGER;
import static org.example.coworking.infrastructure.logger.Log.FILE_LOGGER;

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
        CONSOLE_LOGGER.info("Coworking spaces list:\n");
        coworkingSpaces.forEach(System.out::println);

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
                    CONSOLE_LOGGER.info("You've just made a reservation:\n" + coworkingSpace + ", " + period);
                    isFree = true;
                } catch (TimeOverlapException e) {
                    CONSOLE_LOGGER.warn("Choose other time. The coworking space is unavailable at this time\n");
                    FILE_LOGGER.warn(e.getMessage());
                } catch (InvalidTimeReservationException e) {
                    CONSOLE_LOGGER.warn("You entered invalid date.\nTry again\n");
                    FILE_LOGGER.warn(e.getMessage());
                }
            }
        }
    }

    public void getAllReservations(User customer) {
        List<Reservation> reservations = reservationService.getAllByUser(customer);
        if (reservations.isEmpty()) {
            CONSOLE_LOGGER.warn("Reservation list is empty\n");
            FILE_LOGGER.warn("Reservation list is empty\n");
        } else {
            CONSOLE_LOGGER.info("Your reservation(s):\n");
            reservations.forEach(reservation -> CONSOLE_LOGGER.info(reservation));
        }
    }

    public void delete(BufferedReader reader, User customer) throws IOException {
        List<Reservation> reservationsByCustomer = reservationService.getAllByUser(customer);
        CONSOLE_LOGGER.info("Your reservations:\n");
        if (reservationsByCustomer.isEmpty()) {
            CONSOLE_LOGGER.warn("Reservation list is empty:\n");
            CONSOLE_LOGGER.warn("Reservation list is empty:\n");
        } else {
            reservationsByCustomer.forEach(System.out::println);
            int reservationId = InputValidator.getIntInput(reader, "\nType reservation Id you want to cancel");
            Optional<Reservation> possibleReservation = Optional.empty();
            try {
                possibleReservation = reservationService.getById(reservationId);
            } catch (ReservationNotFoundException e) {
                CONSOLE_LOGGER.warn("Reservation with Id " + reservationId + " is absent\n");
                CONSOLE_LOGGER.warn("Reservation with Id " + reservationId + " is absent\n");
            }
            if (possibleReservation.isPresent()) {
                Reservation reservation = possibleReservation.get();

                try {
                    reservationService.delete(customer, reservation);
                } catch (ForbiddenActionException e) {
                    CONSOLE_LOGGER.warn(e.getMessage() + "Reservation with id: " + reservationId + " belongs to another user");
                    CONSOLE_LOGGER.warn(e.getMessage() + "Reservation with id: " + reservationId + " belongs to another user");
                } catch (ReservationNotFoundException e) {
                    CONSOLE_LOGGER.warn(e.getMessage());
                    FILE_LOGGER.warn(e.getMessage() + "\n");
                }
                CONSOLE_LOGGER.info("Reservation with Id " + reservationId + " is canceled\n");
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
        CONSOLE_LOGGER.info("Choose a CoworkingSpace and type its id to book it:\n");
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
                CONSOLE_LOGGER.error("Invalid date or time value: {}. Please try again.", e.getMessage());
            }
        }
    }

    private int getIntInputInRange(BufferedReader reader, String message, int min, int max) throws IOException {
        while (true) {
            int value = InputValidator.getIntInput(reader, message);
            if (value >= min && value <= max) {
                return value;
            } else {
                CONSOLE_LOGGER.error("Invalid input: must be between {} and {}. Please try again.", min, max);
            }
        }
    }

    private Optional<CoworkingSpace> getCoworkingSpaceFromUser(int coworkingId) {
        try {
            return coworkingService.getById(coworkingId);
        } catch (CoworkingNotFoundException e) {
            CONSOLE_LOGGER.warn(e.getMessage());
            FILE_LOGGER.warn(e.getMessage());
            return Optional.empty();
        }
    }
}