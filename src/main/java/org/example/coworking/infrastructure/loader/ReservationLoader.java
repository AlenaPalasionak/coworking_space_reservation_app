package org.example.coworking.infrastructure.loader;

import org.example.coworking.infrastructure.config.PropertyConfig;

public class ReservationLoader extends AbstractLoaderImpl {

    public ReservationLoader() {
        super(PropertyConfig.getProperties().getProperty("reservations"));
    }
}
