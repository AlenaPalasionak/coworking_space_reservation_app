package org.example.coworking.controller;

import org.example.coworking.service.CoworkingService;
import org.example.coworking.service.CoworkingServiceImpl;
import org.example.coworking.service.ReservationService;
import org.example.coworking.service.ReservationServiceImpl;

public class UserController {
    protected static final CoworkingService coworkingService = new CoworkingServiceImpl();
    protected static final ReservationService reservationService = new ReservationServiceImpl();
}

