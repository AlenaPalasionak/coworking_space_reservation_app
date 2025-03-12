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
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;

import static org.example.coworking.infrastructure.logger.Log.TECHNICAL_LOGGER;
import static org.example.coworking.infrastructure.logger.Log.USER_OUTPUT_LOGGER;

public class ReservationController {
    private final CoworkingService coworkingService;
    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;
    private static final String TRY_AGAIN_MESSAGE = "\nTry again\n";
    private static final String YEAR_PATTERN = "^(202[5-9]|20[3-9][0-9]|2[1-9][0-9]{2}|[3-9][0-9]{3})$";
    private static final String MONTH_PATTERN = "^(0?[1-9]|1[0-2])$";
    private static final String DAY_PATTERN = "^(0?[1-9]|[12][0-9]|3[01])$";
    private static final String HOUR_PATTERN = "^(0?[0-9]|1[0-9]|2[0-3])$";
    private static final String MINUTE_PATTERN = "^([0-5]?[0-9])$";
    private static final String ANY_NUMBER_PATTERN = "\\d+";

    public ReservationController(CoworkingService coworkingService, ReservationService reservationService, ReservationMapper reservationMapper) {
        this.coworkingService = coworkingService;
        this.reservationService = reservationService;
        this.reservationMapper = reservationMapper;
    }

    public void load() {
        reservationService.load();
    }

    public void save() {
        reservationService.save();
    }

    public void add(BufferedReader reader, User customer) throws IOException {
        String coworkingIdInput;
        int coworkingId;
        LocalDateTime startTime;
        LocalDateTime endTime;
        List<CoworkingSpace> coworkingSpaces = coworkingService.getAllByUser(customer);
        String coworkingSpacesAsString = coworkingSpaces.stream()
                .map(CoworkingSpace::toString)
                .reduce((s1, s2) -> s1 + "\n" + s2)
                .orElse("No spaces available");

        while (true) {
            try {
                coworkingIdInput = InputValidator.getInputSupplier(reader, ANY_NUMBER_PATTERN)
                        .supplier(coworkingSpacesAsString + "\n Choose a CoworkingSpace id to book it:\n");
                coworkingId = reservationMapper.getId(coworkingIdInput);
                coworkingService.getById(coworkingId);
                break;
            } catch (InvalidInputException | CoworkingNotFoundException e) {
                USER_OUTPUT_LOGGER.warn(e.getMessage() + TRY_AGAIN_MESSAGE);
                TECHNICAL_LOGGER.warn(e.getMessage());
            }
        }

        startTime = getLocalDateTimeObject(reader);
        endTime = getLocalDateTimeObject(reader);
        try {
            reservationService.add(customer, startTime, endTime, coworkingId);
            USER_OUTPUT_LOGGER.info("You've just made a reservation:\n" + startTime + endTime);
        } catch (TimeOverlapException | InvalidTimeLogicException | CoworkingNotFoundException e) {
            USER_OUTPUT_LOGGER.warn(e.getMessage() + TRY_AGAIN_MESSAGE);
            TECHNICAL_LOGGER.warn(e.getMessage());
        }
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
        String reservationIdInput;
        int reservationId;
        List<Reservation> reservationsByCustomer = reservationService.getAllByUser(customer);
        if (reservationsByCustomer.isEmpty()) {
            USER_OUTPUT_LOGGER.info("Reservation list is empty.\n");
            return;
        }

        boolean isDeleted = false;
        String spacesAsString = reservationsByCustomer.stream()
                .map(Reservation::toString)
                .reduce((s1, s2) -> s1 + "\n" + s2)
                .orElse("");

        while (!isDeleted) {
            try {
                reservationIdInput = InputValidator.getInputSupplier(reader, ANY_NUMBER_PATTERN)
                        .supplier(spacesAsString + "\nType reservation Id you want to cancel\n");
            } catch (InvalidInputException e) {
                USER_OUTPUT_LOGGER.warn(e.getMessage() + TRY_AGAIN_MESSAGE);
                TECHNICAL_LOGGER.warn(e.getMessage());
                continue;
            }

            reservationId = reservationMapper.getId(reservationIdInput);

            try {
                reservationService.delete(customer, reservationId);
                USER_OUTPUT_LOGGER.info("Reservation with Id " + reservationId + " is canceled\n");
                isDeleted = true;
            } catch (ForbiddenActionException | ReservationNotFoundException e) {
                USER_OUTPUT_LOGGER.warn(e.getMessage() + TRY_AGAIN_MESSAGE);
                TECHNICAL_LOGGER.warn(e.getMessage());
            }
        }
    }

    private LocalDateTime getLocalDateTimeObject(BufferedReader reader) {
        String year;
        String month;
        String day;
        String hour;
        String minute;
        LocalDateTime time;
        while (true) {
            try {
                year = InputValidator.getInputSupplier(reader, YEAR_PATTERN).supplier("Type a year. Format: yyyy");
                month = InputValidator.getInputSupplier(reader, MONTH_PATTERN).supplier("Type a month. Format: mm");
                day = InputValidator.getInputSupplier(reader, DAY_PATTERN).supplier("Type a day. Format: dd");
                hour = InputValidator.getInputSupplier(reader, HOUR_PATTERN).supplier("Type  hour. Format: hh");
                minute = InputValidator.getInputSupplier(reader, MINUTE_PATTERN).supplier("Type minute. Format: mm");
                time = reservationMapper.getLocalDateTime(year, month, day, hour, minute);
                break;
            } catch (InvalidInputException | DateTimeException e) {
                USER_OUTPUT_LOGGER.error(e.getMessage() + TRY_AGAIN_MESSAGE);
                TECHNICAL_LOGGER.error(e.getMessage());
            }
        }
        return time;
    }
}