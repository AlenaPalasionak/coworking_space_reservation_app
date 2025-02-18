package org.example.coworking.model;

import lombok.Getter;

@Getter
public class Coworking {
    private final int id;
    private final double price;
    private final boolean isAvailable;
    private final CoworkingType coworkingType;
    private final Facility facility;

    private Coworking(CoworkingSpaceBuilder coworkingSpaceBuilder) {
        this.id = coworkingSpaceBuilder.id;
        this.price = coworkingSpaceBuilder.price;
        this.isAvailable = coworkingSpaceBuilder.isAvailable;
        this.coworkingType = coworkingSpaceBuilder.coworkingType;
        this.facility = coworkingSpaceBuilder.facility;
    }

    public static class CoworkingSpaceBuilder {
        private final int id;
        private final double price;
        private final boolean isAvailable;
        private final CoworkingType coworkingType;
        private Facility facility = null; // Необязательное поле

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

        public Coworking build() {
            return new Coworking(this);
        }
    }

    @Override
    public String toString() {
        return "CoworkingSpace [id=" + id + ", price=" + price + ", available=" + isAvailable + ", type="
                + coworkingType + ", " + facility + "]";
    }
}
