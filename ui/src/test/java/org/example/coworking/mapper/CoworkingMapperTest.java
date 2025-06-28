package org.example.coworking.mapper;

import org.example.coworking.dto.CoworkingSpaceDto;
import org.example.coworking.entity.CoworkingSpace;
import org.example.coworking.entity.CoworkingType;
import org.example.coworking.entity.Facility;
import org.example.coworking.entity.User;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class CoworkingMapperTest {

    private final CoworkingMapper coworkingMapper = new CoworkingMapper();

    @Test
    public void testDtoToEntity() {
        CoworkingSpaceDto dto = new CoworkingSpaceDto();
        dto.setAdminId(42L);
        dto.setPrice(100.0);
        dto.setCoworkingType(CoworkingType.CO_LIVING);
        dto.setFacilities(Set.of(Facility.WIFI, Facility.KITCHEN));

        CoworkingSpace entity = coworkingMapper.coworkingSpaceDtoToEntity(dto);

        assertThat(entity.getAdmin().getId()).isEqualTo(42L);
        assertThat(entity.getPrice()).isEqualTo(100.0);
        assertThat(entity.getCoworkingType()).isEqualTo(CoworkingType.CO_LIVING);
        assertThat(entity.getFacilities()).containsExactlyInAnyOrder(Facility.WIFI, Facility.KITCHEN);
    }

    @Test
    public void testEntityToDto() {
        User admin = new User();
        admin.setId(99L);

        CoworkingSpace space = new CoworkingSpace();
        space.setAdmin(admin);
        space.setPrice(75.0);
        space.setCoworkingType(CoworkingType.OPEN_SPACE);
        space.setFacilities(Set.of(Facility.CONDITIONING, Facility.WIFI));

        CoworkingSpaceDto dto = coworkingMapper.coworkingSpaceToDtoEntity(space);

        assertThat(dto.getAdminId()).isEqualTo(99L);
        assertThat(dto.getPrice()).isEqualTo(75.0);
        assertThat(dto.getCoworkingType()).isEqualTo(CoworkingType.OPEN_SPACE);
        assertThat(dto.getFacilities()).containsExactlyInAnyOrder(Facility.CONDITIONING, Facility.WIFI);
    }

    @Test
    public void testListConversion() {
        User admin = new User();
        admin.setId(1L);

        CoworkingSpace space1 = new CoworkingSpace();
        space1.setAdmin(admin);
        space1.setPrice(50.0);
        space1.setCoworkingType(CoworkingType.CO_LIVING);
        space1.setFacilities(Set.of(Facility.KITCHEN));

        CoworkingSpace space2 = new CoworkingSpace();
        space2.setAdmin(admin);
        space2.setPrice(70.0);
        space2.setCoworkingType(CoworkingType.OPEN_SPACE);
        space2.setFacilities(Set.of(Facility.WIFI));

        List<CoworkingSpaceDto> dtoList = coworkingMapper.coworkingSpacesToDtoList(List.of(space1, space2));

        assertThat(dtoList).hasSize(2);
        assertThat(dtoList.get(0).getPrice()).isEqualTo(50.0);
        assertThat(dtoList.get(1).getCoworkingType()).isEqualTo(CoworkingType.OPEN_SPACE);
    }
}
