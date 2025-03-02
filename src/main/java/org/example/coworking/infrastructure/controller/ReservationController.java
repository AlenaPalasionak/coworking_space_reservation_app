package org.example.coworking.infrastructure.controller;

import org.example.coworking.model.CoworkingSpace;
import org.example.coworking.model.Reservation;
import org.example.coworking.model.ReservationPeriod;
import org.example.coworking.model.User;
import org.example.coworking.service.CoworkingService;
import org.example.coworking.service.CoworkingServiceImpl;
import org.example.coworking.service.ReservationService;
import org.example.coworking.service.ReservationServiceImpl;
import org.example.coworking.service.exception.CoworkingNotFoundException;
import org.example.coworking.service.exception.ForbiddenActionException;
import org.example.coworking.service.exception.ReservationNotFoundException;
import org.example.coworking.service.exception.TimeOverlapException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ReservationController {
    CoworkingService coworkingService;
    ReservationService reservationService;

    public ReservationController() {
        this.coworkingService = new CoworkingServiceImpl();
        this.reservationService = new ReservationServiceImpl();
    }

    public void add(BufferedReader reader, BufferedWriter writer, User customer) throws IOException {
        boolean isFree = false;
        boolean isFound = false;
        List<CoworkingSpace> coworkingSpaces = coworkingService.getAllSpaces();
        writer.write("Coworking spaces list: " + coworkingSpaces);
        while (!isFree && !isFound) {
            int coworkingId = getCoworkingIdFromUser(reader, writer);
            LocalDateTime startTime = getDateTimeFromUser(reader, writer, "start");
            LocalDateTime endTime = getDateTimeFromUser(reader, writer, "end");

            ReservationPeriod period = new ReservationPeriod(startTime, endTime);

            CoworkingSpace coworkingSpace = getCoworkingSpaceFromUser(writer, coworkingId);
            if (coworkingSpace != null) {
                isFound = true;

                try {
                    reservationService.add(customer, coworkingSpace, period);
                    writer.write("You just made a reservation:\n");
                    writer.flush();
                    isFree = true;
                } catch (TimeOverlapException e) {
                    writer.write("Choose another time. The coworking space is unavailable at this time\n");
                }
            }
        }
    }

    public void getAllReservations(BufferedWriter writer, User customer) throws IOException {
        List<Reservation> reservations = reservationService.getAllReservations(customer);
        //TODO add exception to service
        if (reservations.isEmpty()) {
            writer.write("Reservation list is empty\n");
            writer.flush();
        } else {
            writer.write("Your reservation(s):\n" + reservations);
            writer.flush();
        }
    }

    public void delete(BufferedReader reader, BufferedWriter writer, User customer) throws IOException {
        List<Reservation> reservationsByCustomer = reservationService.getAllReservations(customer);
        writer.write("Your reservations:\n" + reservationsByCustomer + "\nType reservation Id you want to cancel");
        writer.flush();

        int reservationId = Integer.parseInt(reader.readLine());
        Optional<Reservation> possibleReservation = Optional.empty();
        try {
            possibleReservation = reservationService.getReservationByReservationId(reservationId);
        } catch (ReservationNotFoundException e) {
            writer.write("Reservation with Id " + reservationId + " is absent\n");
        }
        if (possibleReservation.isPresent()) {
            Reservation reservation = possibleReservation.get();
            CoworkingSpace coworkingSpace = reservation.getCoworkingSpace();

            try {
                reservationService.delete(reservation, customer, coworkingSpace);
            } catch (ForbiddenActionException e) {
                throw new RuntimeException(e);
            }
            writer.write("Reservation with Id " + reservationId + " is canceled\n");
            writer.flush();
        }
    }

    private int getCoworkingIdFromUser(BufferedReader reader, BufferedWriter writer) throws IOException {
        writer.write("Choose a CoworkingSpace and type its id to book it:\n");
        writer.flush();
        return Integer.parseInt(reader.readLine());
    }

    private LocalDateTime getDateTimeFromUser(BufferedReader reader, BufferedWriter writer, String timeType) throws IOException {
        writer.write("Type a year. Format: yyyy\n");
        writer.flush();
        int year = Integer.parseInt(reader.readLine());

        writer.write("Type a month. Format: m or mm\n");
        writer.flush();
        int month = Integer.parseInt(reader.readLine());

        writer.write("Type a day. Format: d or dd\n");
        writer.flush();
        int day = Integer.parseInt(reader.readLine());

        writer.write("Type " + timeType + " hour. Format: h or hh\n");
        writer.flush();
        int hour = Integer.parseInt(reader.readLine());

        writer.write("Type " + timeType + " minute. Format: m or mm\n");
        writer.flush();
        int minute = Integer.parseInt(reader.readLine());

        return LocalDateTime.of(year, month, day, hour, minute);
    }

    private CoworkingSpace getCoworkingSpaceFromUser(BufferedWriter writer, int coworkingId) throws IOException {
        try {
            return coworkingService.getById(coworkingId);
        } catch (CoworkingNotFoundException e) {
            writer.write("There is no space with id:" + coworkingId);
            return null;
        }
    }
}