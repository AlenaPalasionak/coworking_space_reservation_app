package org.example.coworking.model;

import java.util.EnumSet;
import java.util.Set;

public class Facility {
    private final Set<Feature> features;

    private Facility(FacilityBuilder facilityBuilder) {
        this.features = facilityBuilder.features;
    }

    public enum Feature {
        PARKING,
        WIFI,
        KITCHEN,
        PRINTER,
        CONDITIONING
    }

    @Override
    public String toString() {
        return "Facility features: " + features;
    }

    public static class FacilityBuilder {
        private final Set<Feature> features = EnumSet.noneOf(Feature.class);

        public void addFeature(Feature feature) {
            features.add(feature);
        }

        public Facility build() {
            return new Facility(this);
        }

        @Override
        public String toString() {
            return "Facility: " + features;
        }
    }
}
