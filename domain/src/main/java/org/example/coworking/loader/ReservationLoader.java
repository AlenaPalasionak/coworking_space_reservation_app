package org.example.coworking.loader;

import org.example.coworking.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class ReservationLoader extends AbstractLoaderImpl<Reservation> {
        private final String filePath;


    @Autowired
    public ReservationLoader(@Value("${reservation.path}") String filePath) {
        super();
        this.filePath = filePath;
    }

        @Override
        protected String getFilepath() {
            return filePath;
        }
    }
