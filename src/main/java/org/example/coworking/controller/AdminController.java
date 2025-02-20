package org.example.coworking.controller;

import org.example.coworking.model.Coworking;
import org.example.coworking.model.Reservation;

import java.util.List;
import java.util.Optional;

public class AdminController extends UserController {

    public AdminController() {
        super();
    }

    public void addCoworkingSpace(Coworking space) {
        coworkingService.addSpace(space);
    }

    public void removeCoworkingSpace(int id) {
        coworkingService.removeSpace(id);
    }

    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    public void updateCoworkingSpace(Coworking newCoworking, int toReplaceCoworkingId) {
        coworkingService.updateSpace(newCoworking, toReplaceCoworkingId);
    }

    public Coworking getSpaceById(int id) {
        return coworkingService.getById(id);
    }

    public List<Coworking> getAllCoworkingSpaces() {
        return coworkingService.getAllSpaces();
    }

    public Optional<Reservation> getReservationById(int reservationId) {
        return reservationService.getReservationById(reservationId);
    }
}

