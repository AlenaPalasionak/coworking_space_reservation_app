package org.example.coworking.infrastructure.json_loader;

import org.example.coworking.infrastructure.config.PropertyConfig;

public class ReservationJsonLoader extends AbstractJsonLoaderImpl {

    public ReservationJsonLoader() {
        super(PropertyConfig.getProperties().getProperty("reservations"));
    }
}
