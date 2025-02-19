package org.example.coworking.util;

import org.example.coworking.controller.AdminController;
import org.example.coworking.controller.CustomerController;
import org.example.coworking.model.Coworking;
import org.example.coworking.model.Customer;
import org.example.coworking.model.Reservation;
import org.example.coworking.model.ReservationPeriod;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class CustomerControllerHelper {
    static CustomerController customerController = new CustomerController();
    static AdminController adminController = new AdminController();

    public static void cancelReservation(BufferedReader reader, BufferedWriter writer, Customer customer) throws IOException {
        writer.write("""
                 Your reservations:
                """ + customerController.getReservationsByCustomer(customer.getId()));
        writer.write("""
                Type reservation Id you want to cancel)
                """);
        writer.flush();
        int reservationId = Integer.parseInt(reader.readLine());
        customerController.canselReservation(reservationId);
        writer.write("""
                Reservation with Id is canceled
                """);
        writer.flush();
    }

    public static String showCustomerMenu(BufferedReader reader, BufferedWriter writer) throws IOException {
        writer.write(""" 
                                
                Press 1 to browse available spaces.
                Press 2 to Make a reservation.
                Press 3 to view your reservations.
                Press 4 to cancel a reservation.
                                
                """);
        writer.flush();
        return reader.readLine();
    }

    public static Customer customerLogIn(BufferedReader reader, BufferedWriter writer) throws IOException {
        Customer customer = null;
        boolean isLoggedIn = false;
        while (!isLoggedIn) {
            writer.write("""
                                
                    Enter your customer name, please.
                                        
                    """);
            writer.flush();
            String customerName = reader.readLine().trim();
            writer.write(customerName + ", " + """
                    Enter your customer password, please.
                                        
                    """);
            writer.flush();
            String customerPassword = reader.readLine().trim();
            if (PasswordValidator.isCustomerLoginDataValid(customerName, customerPassword)) {
                isLoggedIn = true;
                int customerId = IdGenerator.generateUserId();
                customer = new Customer(customerId, customerName, customerPassword);
                writer.write("""
                                                
                        You have successfully logged in.
                                                
                        """);
                writer.flush();
            } else {
                writer.write("""
                                                
                        Your login data are wrong. Press Enter to try again
                                                
                        """);
                writer.flush();
            }
        }
        return customer;
    }

    public static void addReservation(BufferedReader reader, BufferedWriter writer, Customer customer) throws
            IOException {
        if (adminController.getAllReservations().isEmpty()) {
            writer.write("""
                    The are no free spaces to book
                    """);
            writer.flush();
        } else {
            writer.write("""
                    Choose a Coworking and type its id to book it
                    """);
            writer.flush();
            int coworkingId = Integer.parseInt(reader.readLine());
            writer.write("""
                    Type a year. Format: yyyy
                    """);
            writer.flush();
            int year = Integer.parseInt(reader.readLine());
            writer.write("""
                    Type a month. Format: m or mm
                    """);
            writer.flush();
            int month = Integer.parseInt(reader.readLine());
            writer.write("""
                    Type a day. Format: d or dd
                    """);
            writer.flush();
            int day = Integer.parseInt(reader.readLine());
            writer.write("""
                    Type start hour. Format: h or hh
                    """);
            writer.flush();
            int startHour = Integer.parseInt(reader.readLine());
            writer.write("""
                    Type end hour. Format: h or hh
                    """);
            writer.flush();
            int endHour = Integer.parseInt(reader.readLine());
            LocalTime endTime;
            int minute = 0;
            int singleCoworkingIndex = 0;
            ReservationPeriod period = new ReservationPeriod(
                    LocalDate.of(year, month, day),
                    LocalTime.of(startHour, minute),
                    LocalTime.of(endHour, minute)
            );
            List<Coworking> allCoworkings = adminController.getAllCoworkingSpaces();
            List<Coworking> coworkingToReserve = allCoworkings.stream()
                    .filter(coworking -> coworking.getId() == coworkingId).toList();
            Reservation reservation = new Reservation(IdGenerator.generateReservationId()
                    , customer, coworkingId, period, coworkingToReserve.get(singleCoworkingIndex));
            customerController.addReservation(reservation);
        }
    }

    public static List<Reservation> getCustomerReservations(BufferedReader reader, BufferedWriter writer, Customer
            customer) throws IOException {
        List<Reservation> reservations = customerController.getReservationsByCustomer(customer.getId());
        if (reservations.isEmpty()) {
            writer.write("""
                    You haven't made any reservations yet
                    """);
            writer.flush();
        }

        return reservations;
    }

    public static List<Coworking> getAllCoworkingSpaces(BufferedReader reader, BufferedWriter writer) throws
            IOException {
        List<Coworking> coworkingSpaces = adminController.getAllCoworkingSpaces();
        if (coworkingSpaces.isEmpty()) {
            writer.write("""
                    You haven't made any reservations yet
                    """);
            writer.flush();
        }

        return coworkingSpaces;
    }
}
