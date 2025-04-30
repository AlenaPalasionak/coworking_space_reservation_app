package org.example.coworking.mapper;

import org.example.coworking.dto.CoworkingSpaceDto;
import org.example.coworking.entity.CoworkingSpace;
import org.example.coworking.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The ReservationMapper class is responsible for mapping input data, typically in string format,
 * to specific objects and values related to reservations, such as LocalDateTime and Long identifiers.
 * This class provides utility methods to convert string inputs into a `LocalDateTime` object
 * and to parse and validate identifiers as `Long` values.
 */
@Component
public class CoworkingMapper {

    public List<CoworkingSpaceDto> coworkingSpacesToDtoList(List<CoworkingSpace> coworkingSpaces) {
        return coworkingSpaces.stream()
                .map(this::coworkingSpaceToDtoEntity)
                .collect(Collectors.toList());
    }

    public CoworkingSpace coworkingSpaceDtoToEntity(CoworkingSpaceDto dto) {
        CoworkingSpace coworkingSpace = new CoworkingSpace();
        User admin = new User();
        admin.setId(dto.getAdminId());
        coworkingSpace.setAdmin(admin);
        coworkingSpace.setPrice(dto.getPrice());
        coworkingSpace.setCoworkingType(dto.getCoworkingType());
        coworkingSpace.setFacilities(dto.getFacilities());

        return coworkingSpace;
    }

    public CoworkingSpaceDto coworkingSpaceToDtoEntity(CoworkingSpace space) {
        CoworkingSpaceDto dto = new CoworkingSpaceDto();
        dto.setAdminId(space.getAdmin().getId());
        dto.setPrice(space.getPrice());
        dto.setCoworkingType(space.getCoworkingType());
        dto.setFacilities(space.getFacilities());
        return dto;
    }
}

