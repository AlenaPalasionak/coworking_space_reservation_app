package org.example.coworking.infrastructure.util.json_loader;

import org.example.coworking.infrastructure.config.Config;

public class ReservationLoaderImpl extends AbstractJsonLoader {

    public ReservationLoaderImpl() {
        super(Config.getProperties().getProperty("reservations"));
    }
}
