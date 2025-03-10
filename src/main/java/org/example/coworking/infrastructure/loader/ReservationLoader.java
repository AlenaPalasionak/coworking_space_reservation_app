package org.example.coworking.infrastructure.loader;

import org.example.coworking.model.Reservation;

public class ReservationLoader extends AbstractLoaderImpl<Reservation> {
        private final String filePath;

        public ReservationLoader(String filePath) {
            super();
            this.filePath = filePath;
        }

        @Override
        protected String getFilepath() {
            return filePath;
        }
    }
