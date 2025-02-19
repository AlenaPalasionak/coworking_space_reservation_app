package org.example.coworking.controller;

import org.example.coworking.model.Coworking;
import org.example.coworking.service.CoworkingService;
import org.example.coworking.service.CoworkingServiceImpl;
import org.example.coworking.service.ReservationService;
import org.example.coworking.service.ReservationServiceImpl;

import java.util.List;

public abstract class AbstractUserController {
    protected static final CoworkingService coworkingService = new CoworkingServiceImpl();
    protected static final ReservationService reservationService = new ReservationServiceImpl();
    public  List<Coworking> getAllCoworkingSpaces() {
        return coworkingService.getAllSpaces();
    }
}

