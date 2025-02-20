package org.example.coworking.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Coworking {
    private final int id;
    private final double price;
    private final boolean isAvailable;
    private final CoworkingType coworkingType;
    private final Facility facility;
    private final List<ReservationPeriod> reservationsPeriods;

    private Coworking(CoworkingSpaceBuilder coworkingSpaceBuilder) {
        this.id = coworkingSpaceBuilder.id;
        this.price = coworkingSpaceBuilder.price;
        this.isAvailable = coworkingSpaceBuilder.isAvailable;
        this.coworkingType = coworkingSpaceBuilder.coworkingType;
        this.facility = coworkingSpaceBuilder.facility;
        this.reservationsPeriods = new ArrayList<>(coworkingSpaceBuilder.reservationsPeriods);
    }

    public void addReservationPeriods(List<ReservationPeriod> periods) {
        List<ReservationPeriod> updatedPeriods = new ArrayList<>(this.reservationsPeriods);
        updatedPeriods.addAll(periods);
        new CoworkingSpaceBuilder(this.id, this.price, this.isAvailable, this.coworkingType)
                .setFacility(this.facility)
                .setReservationsPeriods(updatedPeriods)
                .build();
    }

    public static class CoworkingSpaceBuilder {
        private final int id;
        private final double price;
        private final boolean isAvailable;
        private final CoworkingType coworkingType;
        private Facility facility = null;
        private final List<ReservationPeriod> reservationsPeriods = new ArrayList<>();

        public CoworkingSpaceBuilder(int id, double price, boolean isAvailable, CoworkingType coworkingType) {
            this.id = id;
            this.price = price;
            this.isAvailable = isAvailable;
            this.coworkingType = coworkingType;
        }

        public CoworkingSpaceBuilder setFacility(Facility facility) {
            this.facility = facility;
            return this;
        }

        public CoworkingSpaceBuilder addReservationPeriods(List<ReservationPeriod> periods) {
            this.reservationsPeriods.addAll(periods);
            return this;
        }

        // Новый метод для установки списка бронирований
        public CoworkingSpaceBuilder setReservationsPeriods(List<ReservationPeriod> periods) {
            this.reservationsPeriods.clear();
            this.reservationsPeriods.addAll(periods);
            return this;
        }

        public Coworking build() {
            return new Coworking(this);
        }
    }

    @Override
    public String toString() {
        return " Coworking { " +
                "id=" + id +
                ", price=" + price +
                ", isAvailable=" + isAvailable +
                ", coworkingType=" + coworkingType +
                ", facility=" + facility +
                ", reservationsPeriods=" + reservationsPeriods  +
                "}\n";
    }
}

//@Getter
//public class Coworking {
//    private final int id;
//    private final double price;
//    private final boolean isAvailable;
//    private final CoworkingType coworkingType;
//    private final Facility facility;
//    private final List<Period> reservationsPeriods;
//
//    private Coworking(CoworkingSpaceBuilder coworkingSpaceBuilder) {
//        this.id = coworkingSpaceBuilder.id;
//        this.price = coworkingSpaceBuilder.price;
//        this.isAvailable = coworkingSpaceBuilder.isAvailable;
//        this.coworkingType = coworkingSpaceBuilder.coworkingType;
//        this.facility = coworkingSpaceBuilder.facility;
//        this.reservationsPeriods = new ArrayList<>(coworkingSpaceBuilder.reservationsPeriods); // Создание копии списка
//    }
//
//    // Метод для добавления периода бронирования
//    public Coworking addReservationPeriod(Period period) {
//        List<Period> updatedPeriods = new ArrayList<>(this.reservationsPeriods);
//        updatedPeriods.add(period);
//        return new Coworking.CoworkingSpaceBuilder(this.id, this.price, this.isAvailable, this.coworkingType)
//                .setFacility(this.facility)
//                .setReservationsPeriods(updatedPeriods)
//                .build();
//    }
//
//    public static class CoworkingSpaceBuilder {
//        private final int id;
//        private final double price;
//        private final boolean isAvailable;
//        private final CoworkingType coworkingType;
//        private Facility facility = null;
//        private final List<Period> reservationsPeriods = new ArrayList<>();
//
//        public CoworkingSpaceBuilder(int id, double price, boolean isAvailable, CoworkingType coworkingType) {
//            this.id = id;
//            this.price = price;
//            this.isAvailable = isAvailable;
//            this.coworkingType = coworkingType;
//        }
//
//        public CoworkingSpaceBuilder setFacility(Facility facility) {
//            this.facility = facility;
//            return this;
//        }
//
//        public CoworkingSpaceBuilder addReservationPeriod(Period period) {
//            this.reservationsPeriods.add(period);
//            return this;
//        }
//
//        // Новый метод для установки списка бронирований
//        public CoworkingSpaceBuilder setReservationsPeriods(List<Period> periods) {
//            this.reservationsPeriods.clear();
//            this.reservationsPeriods.addAll(periods);
//            return this;
//        }
//
//        public Coworking build() {
//            return new Coworking(this);
//        }
//    }
//
//    @Override
//    public String toString() {
//        return "Coworking{" +
//                "id=" + id +
//                ", price=" + price +
//                ", isAvailable=" + isAvailable +
//                ", coworkingType=" + coworkingType +
//                ", facility=" + facility +
//                ", reservationsPeriods=" + reservationsPeriods +
//                '}';
//    }
//}
//
