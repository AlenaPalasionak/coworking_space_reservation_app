package org.example.coworking.controller;

import org.example.coworking.controller.exception.InvalidInputException;
import org.example.coworking.controller.validator.InputValidator;
import org.example.coworking.repository.exception.EntityNotFoundException;
import org.example.coworking.mapper.ReservationMapper;
import org.example.coworking.entity.*;
import org.example.coworking.service.CoworkingService;
import org.example.coworking.service.ReservationService;
import org.example.coworking.service.exception.ForbiddenActionException;
import org.example.coworking.service.exception.ReservationTimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;

import static org.example.coworking.logger.Log.TECHNICAL_LOGGER;
import static org.example.coworking.logger.Log.USER_OUTPUT_LOGGER;

/**
 * The ReservationController class manages reservations for customers, allowing them to create, view, and delete reservations
 * for coworking spaces. This class interacts with the reservation service, coworking service, and reservation mapper to
 * perform actions related to reservations.
 */
@Component
public class ReservationController {
    private final CoworkingService coworkingService;
    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;
    private static final String YEAR_PATTERN = "20(2[5-9]|[3-9][0-9])";
    private static final String MONTH_PATTERN = "^(0?[1-9]|1[0-2])$";
    private static final String DAY_PATTERN = "^(0?[1-9]|[12][0-9]|3[01])$";
    private static final String HOUR_PATTERN = "^(0?[0-9]|1[0-9]|2[0-3])$";
    private static final String MINUTE_PATTERN = "^([0-5]?[0-9])$";
    private static final String ANY_NUMBER_PATTERN = "\\d+";

    /**
     * Constructs a ReservationController object with the specified services and mapper.
     *
     * @param coworkingService   the service responsible for managing coworking spaces
     * @param reservationService the service responsible for managing reservations
     * @param reservationMapper  the mapper for converting input to reservation objects
     */

    @Autowired
    public ReservationController(CoworkingService coworkingService, ReservationService reservationService, ReservationMapper reservationMapper) {
        this.coworkingService = coworkingService;
        this.reservationService = reservationService;
        this.reservationMapper = reservationMapper;
    }

    /**
     * Allows a customer to add a reservation for a coworking space by specifying the start and end times.
     * Prompts the user for input and validates the entered values.
     *
     * @param reader   the BufferedReader for user input
     * @param customer the customer making the reservation
     */
    public void add(BufferedReader reader, Customer customer) {
        String coworkingIdInput;
        Long coworkingId;
        LocalDateTime startTime;
        LocalDateTime endTime;
        List<CoworkingSpace> coworkingSpaces = coworkingService.getAll();

        if (coworkingSpaces.isEmpty()) {
            USER_OUTPUT_LOGGER.info("Coworking Spaces list is empty.\n");
            return;
        }

        String spacesAsString = coworkingSpaces.stream()
                .map(CoworkingSpace::toString)
                .reduce((s1, s2) -> s1 + "\n" + s2)
                .orElse("");

        while (true) {
            try {
                coworkingIdInput = InputValidator.getInputSupplier(reader, ANY_NUMBER_PATTERN)
                        .supplier(spacesAsString + "\n Choose a CoworkingSpace id to book it:\n");
                coworkingId = reservationMapper.getId(coworkingIdInput);
                coworkingService.getById(coworkingId);
                break;
            } catch (InvalidInputException e) {
                USER_OUTPUT_LOGGER.warn(e.getErrorCode());
                TECHNICAL_LOGGER.warn(e.getMessage());
            } catch (EntityNotFoundException e) {
                USER_OUTPUT_LOGGER.warn(e.getErrorCode());
                TECHNICAL_LOGGER.warn(e.getMessage());
            }
        }

        while (true) {
            try {
                startTime = getLocalDateTimeObject(reader);
                break;
            } catch (InvalidInputException | DateTimeException e) {
                USER_OUTPUT_LOGGER.warn("You entered invalid date. Try again\n");
                TECHNICAL_LOGGER.warn(e.getMessage());
            }
        }

        while (true) {
            try {
                endTime = getLocalDateTimeObject(reader);
                break;
            } catch (InvalidInputException | DateTimeException e) {
                USER_OUTPUT_LOGGER.warn("You entered invalid date. Try again\n");
                TECHNICAL_LOGGER.warn(e.getMessage());
            }
        }

        try {
            reservationService.add(customer, startTime, endTime, coworkingId);
            USER_OUTPUT_LOGGER.info("You've just made a reservation:\n" + startTime + endTime);
        } catch (EntityNotFoundException e) {
            USER_OUTPUT_LOGGER.warn(e.getErrorCode());
            TECHNICAL_LOGGER.warn(e.getMessage());
        } catch (ReservationTimeException e) {
            USER_OUTPUT_LOGGER.warn(e.getErrorCode());
            TECHNICAL_LOGGER.warn(e.getMessage());
        }
    }

    /**
     * Displays all reservations made by the specified customer.
     *
     * @param admin the customer whose reservations are to be displayed
     */
    public void getAllReservationsByAdmin(Admin admin) {
        List<Reservation> reservations = reservationService.getAllReservationsByAdmin(admin);
        if (reservations.isEmpty()) {
            USER_OUTPUT_LOGGER.warn("Reservation list is empty\n");
            TECHNICAL_LOGGER.warn("Reservation list is empty\n");
        } else {
            USER_OUTPUT_LOGGER.info("Your reservation(s):\n");
            reservations.forEach(USER_OUTPUT_LOGGER::info);
        }
    }

    /**
     * Displays all reservations made by the specified customer.
     *
     * @param customer the customer whose reservations are to be displayed
     */
    public void getAllReservationsByCustomer(Customer customer) {
        List<Reservation> reservations = reservationService.getAllReservationsByCustomer(customer);
        if (reservations.isEmpty()) {
            USER_OUTPUT_LOGGER.warn("Reservation list is empty\n");
            TECHNICAL_LOGGER.warn("Reservation list is empty\n");
        } else {
            USER_OUTPUT_LOGGER.info("Your reservation(s):\n");
            reservations.forEach(USER_OUTPUT_LOGGER::info);
        }
    }

    /**
     * Allows a customer to delete a reservation by specifying its reservation ID.
     * Prompts the user for input and validates the entered values.
     *
     * @param reader   the BufferedReader for user input
     * @param customer the customer deleting the reservation
     */
    public void delete(BufferedReader reader, Customer customer) {
        String reservationIdInput;
        Long reservationId;
        List<Reservation> reservationsByCustomer = reservationService.getAllReservationsByCustomer(customer);
        if (reservationsByCustomer.isEmpty()) {
            USER_OUTPUT_LOGGER.info("Reservation list is empty.\n");
            return;
        }

        String spacesAsString = reservationsByCustomer.stream()
                .map(Reservation::toString)
                .reduce((s1, s2) -> s1 + "\n" + s2)
                .orElse("");

        while (true) {
            try {
                reservationIdInput = InputValidator.getInputSupplier(reader, ANY_NUMBER_PATTERN)
                        .supplier(spacesAsString + "\nType reservation Id you want to cancel\n");
                reservationId = reservationMapper.getId(reservationIdInput);
                reservationService.delete(customer, reservationId);
                USER_OUTPUT_LOGGER.info("Reservation with Id " + reservationId + " is canceled\n");
                break;
            } catch (InvalidInputException e) {
                USER_OUTPUT_LOGGER.warn(e.getErrorCode());
                TECHNICAL_LOGGER.warn(e.getMessage());
            } catch (EntityNotFoundException e) {
                USER_OUTPUT_LOGGER.warn(e.getErrorCode());
                TECHNICAL_LOGGER.warn(e.getMessage());
            } catch (ForbiddenActionException e) {
                USER_OUTPUT_LOGGER.warn(e.getErrorCode());
                TECHNICAL_LOGGER.warn(e.getMessage());
            }
        }
    }

    /**
     * Prompts the user for a valid date and time and returns the corresponding LocalDateTime object.
     *
     * @param reader the BufferedReader for user input
     * @return the LocalDateTime object corresponding to the input date and time
     * @throws InvalidInputException if the input is invalid
     * @throws DateTimeException     if the date/time is invalid
     */
    private LocalDateTime getLocalDateTimeObject(BufferedReader reader) throws InvalidInputException, DateTimeException {
        String year;
        String month;
        String day;
        String hour;
        String minute;
        LocalDateTime time;

        year = InputValidator.getInputSupplier(reader, YEAR_PATTERN).supplier("Type a year. Format: yyyy");
        month = InputValidator.getInputSupplier(reader, MONTH_PATTERN).supplier("Type a month. Format: mm");
        day = InputValidator.getInputSupplier(reader, DAY_PATTERN).supplier("Type a day. Format: dd");
        hour = InputValidator.getInputSupplier(reader, HOUR_PATTERN).supplier("Type hour. Format: hh");
        minute = InputValidator.getInputSupplier(reader, MINUTE_PATTERN).supplier("Type minute. Format: mm");
        time = reservationMapper.getLocalDateTime(year, month, day, hour, minute);

        return time;
    }
}
