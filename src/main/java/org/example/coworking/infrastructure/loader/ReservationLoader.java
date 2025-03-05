package org.example.coworking.infrastructure.loader;

import org.example.coworking.infrastructure.config.PropertyConfig;
import org.example.coworking.model.Reservation;

public class ReservationLoader extends AbstractLoaderImpl<Reservation> {

    public ReservationLoader() {
        super(PropertyConfig.getProperties().getProperty("reservations"));
    }
}
