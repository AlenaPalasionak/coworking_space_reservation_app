package org.example.coworking.model;

import java.util.EnumSet;
import java.util.Set;

public class Facility {
    public enum Feature {
        PARKING,
        WIFI,
        KITCHEN,
        PRINTER,
        CONDITIONING
    }

    private final Set<Feature> features;

    private Facility(FacilityBuilder facilityBuilder) {
        this.features = facilityBuilder.features;
    }

    public boolean hasFeature(Feature feature) {
        return features.contains(feature);
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
