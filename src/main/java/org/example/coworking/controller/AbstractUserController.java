package org.example.coworking.controller;

import org.example.coworking.service.CoworkingService;
import org.example.coworking.service.CoworkingServiceImpl;
import org.example.coworking.service.ReservationService;
import org.example.coworking.service.ReservationServiceImpl;

public abstract class AbstractUserController {
    protected final CoworkingService coworkingService;
    protected final ReservationService reservationService;

    public AbstractUserController() {
        this.coworkingService = new CoworkingServiceImpl();
        this.reservationService = new ReservationServiceImpl();
    }
}
