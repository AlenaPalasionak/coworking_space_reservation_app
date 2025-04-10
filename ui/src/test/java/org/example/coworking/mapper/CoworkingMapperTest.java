package org.example.coworking.mapper;

import org.example.coworking.mapper.exception.CoworkingTypeIndexException;
import org.example.coworking.mapper.exception.FacilityTypeIndexException;
import org.example.coworking.model.CoworkingType;
import org.example.coworking.model.Facility;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CoworkingMapperTest {
    private static final CoworkingMapper coworkingMapper = new CoworkingMapper();

    @Test
    public void testGetPrice() {
        double price = coworkingMapper.getPrice("29.99");

        assertThat(price).isEqualTo(29.99);
        assertThatThrownBy(() -> coworkingMapper.getPrice("invalid"))
                .isInstanceOf(NumberFormatException.class);
    }

    @Test
    public void testGetCoworkingTypeValidInput() throws CoworkingTypeIndexException {
        assertThat(coworkingMapper.getCoworkingType("0")).isEqualTo(CoworkingType.values()[0]);
        assertThat(coworkingMapper.getCoworkingType("1")).isEqualTo(CoworkingType.values()[1]);
    }

    @Test
    public void testGetCoworkingTypeInvalidInput() {
        assertThatThrownBy(() -> coworkingMapper.getCoworkingType("20"))
                .isInstanceOf(CoworkingTypeIndexException.class);

        assertThatThrownBy(() -> coworkingMapper.getCoworkingType("-1"))
                .isInstanceOf(CoworkingTypeIndexException.class);
    }

    @Test
    public void testGetFacilityValidInput() throws FacilityTypeIndexException {
        Set<Facility> facilities = coworkingMapper.getFacility("0,1,2");
        assertThat(facilities).hasSize(3)
                .containsExactlyInAnyOrder(
                        Facility.values()[0],
                        Facility.values()[1],
                        Facility.values()[2]);

        facilities = coworkingMapper.getFacility("2,1,0");
        assertThat(facilities).hasSize(3)
                .containsExactlyInAnyOrder(
                        Facility.values()[0],
                        Facility.values()[1],
                        Facility.values()[2]);
    }

    @Test
    public void testGetFacilityEmptyInput() {
        Set<Facility> facilities = coworkingMapper.getFacility("");
        assertThat(facilities).isEmpty();
    }

    @Test
    public void testGetFacilityInvalidInput() {
        assertThatThrownBy(() -> coworkingMapper.getFacility("0,10"))
                .isInstanceOf(FacilityTypeIndexException.class);

        assertThatThrownBy(() -> coworkingMapper.getFacility("1,-1"))
                .isInstanceOf(FacilityTypeIndexException.class);
    }

    @Test
    void testGetFacilityWithDuplicates() throws FacilityTypeIndexException {
        Set<Facility> facilities = coworkingMapper.getFacility("0,0,1,2,2");
        assertThat(facilities)
                .hasSize(3)
                .containsExactlyInAnyOrder(
                        Facility.values()[0],
                        Facility.values()[1],
                        Facility.values()[2]
                );
    }
}
