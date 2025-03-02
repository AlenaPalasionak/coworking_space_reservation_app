package org.example.coworking.infrastructure.json_loader;

import org.example.coworking.infrastructure.config.Config;

public class ReservationLoader extends AbstractJsonLoaderImpl {

    public ReservationLoader() {
        super(Config.getProperties().getProperty("reservations"));
    }
}
