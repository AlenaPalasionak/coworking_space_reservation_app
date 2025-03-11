package org.example.coworking.infrastructure.controller;

import org.example.coworking.infrastructure.controller.exception.InvalidInputException;
import org.example.coworking.infrastructure.controller.validator.InputValidator;
import org.example.coworking.infrastructure.dao.exception.CoworkingNotFoundException;
import org.example.coworking.infrastructure.dao.exception.ReservationNotFoundException;
import org.example.coworking.infrastructure.mapper.ReservationMapper;
import org.example.coworking.model.CoworkingSpace;
import org.example.coworking.model.Reservation;
import org.example.coworking.model.User;
import org.example.coworking.service.CoworkingService;
import org.example.coworking.service.ReservationService;
import org.example.coworking.service.exception.ForbiddenActionException;
import org.example.coworking.service.exception.InvalidTimeLogicException;
import org.example.coworking.service.exception.TimeOverlapException;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.example.coworking.infrastructure.logger.Log.TECHNICAL_LOGGER;
import static org.example.coworking.infrastructure.logger.Log.USER_OUTPUT_LOGGER;

public class ReservationController {
    private final CoworkingService coworkingService;
    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;

    public ReservationController(CoworkingService coworkingService, ReservationService reservationService, ReservationMapper reservationMapper) {
        this.coworkingService = coworkingService;
        this.reservationService = reservationService;
        this.reservationMapper = reservationMapper;
    }

    public void add(BufferedReader reader, User customer) throws IOException {
        String coworkingId;

        boolean isFree = false;
        boolean isFound = false;
        List<CoworkingSpace> coworkingSpaces = coworkingService.getAllByUser(customer);
        String coworkingSpacesAsString = coworkingSpaces.stream()
                .map(CoworkingSpace::toString)
                .reduce((s1, s2) -> s1 + "\n" + s2)
                .orElse("No spaces available");

        while (true) {
            try {
                coworkingId = InputValidator.getInputSupplier(reader, "\\d+")
                        .supplier(coworkingSpacesAsString + "\n Choose a CoworkingSpace and type its id to book it:\n");
                break;
            } catch (InvalidInputException e) {
                USER_OUTPUT_LOGGER.warn(e.getMessage() + " Try again:\n");
                TECHNICAL_LOGGER.warn(e.getMessage());
            }
        }

        LocalDateTime startTime = getLocalDateTimeObject(reader);
        LocalDateTime endTime = getLocalDateTimeObject(reader);

        try {//TODO think wehere to add Coworking and
            return coworkingService.getById(coworkingId);
        } catch (CoworkingNotFoundException e) {
            USER_OUTPUT_LOGGER.warn(e.getMessage());
            TECHNICAL_LOGGER.warn(e.getMessage());
            return Optional.empty();
        }
        Optional<CoworkingSpace> possibleCoworkingSpace
        if (possibleCoworkingSpace.isPresent()) {
            isFound = true;
            try {
                CoworkingSpace coworkingSpace = possibleCoworkingSpace.get();
                reservationService.add(startTime, endTime, customer, coworkingSpace);
                USER_OUTPUT_LOGGER.info("You've just made a reservation:\n" + coworkingSpace + ", " + startTime + endTime);
                isFree = true;
            } catch (TimeOverlapException e) {
                USER_OUTPUT_LOGGER.warn("Choose other time. The coworking space is unavailable at this time\n");
                TECHNICAL_LOGGER.warn(e.getMessage());
            } catch (InvalidTimeLogicException e) {
                USER_OUTPUT_LOGGER.warn("You entered invalid date.\nTry again\n");
                TECHNICAL_LOGGER.warn(e.getMessage());
            }
        }
    }

    public LocalDateTime getLocalDateTimeObject(BufferedReader reader) {
        String year;
        String month;
        String day;
        String hour;
        String minute;
        LocalDateTime time;
        while (true) {
            try {
                year = InputValidator.getInputSupplier(reader, "^\\d{4}$").supplier("Type a year. Format: yyyy");
                month = InputValidator.getInputSupplier(reader, "^^(1[0-2]|[1-9])$").supplier("Type a month. Format: m or mm");
                day = InputValidator.getInputSupplier(reader, "^(3[01]|[12][0-9]|[1-9])$").supplier("Type a day. Format: d or dd");
                hour = InputValidator.getInputSupplier(reader, "^(2[0-3]|[01]?[0-9])$").supplier("Type  hour. Format: h or hh");
                minute = InputValidator.getInputSupplier(reader, "Э^(5[0-9]|[0-4]?[0-9])$").supplier("Type minute. Format: m or mm");
                time = reservationMapper.getLocalDateTime(year, month, day, hour, minute);
                break;
            } catch (InvalidInputException e) {
                USER_OUTPUT_LOGGER.error(e.getMessage() + " Try again: \n");
                TECHNICAL_LOGGER.error(e.getMessage());
            }
        }
        return time;
    }

    public void getAllReservations(User customer) {
        List<Reservation> reservations = reservationService.getAllByUser(customer);
        if (reservations.isEmpty()) {
            USER_OUTPUT_LOGGER.warn("Reservation list is empty\n");
            TECHNICAL_LOGGER.warn("Reservation list is empty\n");
        } else {
            USER_OUTPUT_LOGGER.info("Your reservation(s):\n");
            reservations.forEach(USER_OUTPUT_LOGGER::info);
        }
    }

    public void delete(BufferedReader reader, User customer) throws IOException {
        List<Reservation> reservationsByCustomer = reservationService.getAllByUser(customer);
        USER_OUTPUT_LOGGER.info("Your reservations:\n");
        if (reservationsByCustomer.isEmpty()) {
            USER_OUTPUT_LOGGER.warn("Reservation list is empty:\n");
            TECHNICAL_LOGGER.warn("Reservation list is empty:\n");
        } else {
            reservationsByCustomer.forEach(USER_OUTPUT_LOGGER::info);
            int reservationId = InputValidator.convertInputToInt(reader, "\nType reservation Id you want to cancel");
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
}