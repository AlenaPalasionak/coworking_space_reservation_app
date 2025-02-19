package org.example.coworking.controller;

import org.example.coworking.model.Coworking;
import org.example.coworking.model.Reservation;

import java.util.List;

public class AdminControllerAbstract extends AbstractUserController {

    public AdminControllerAbstract() {
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

    public void updateCoworkingSpace(Coworking space, int id) {
        coworkingService.updateSpace(space, id);
    }
}

