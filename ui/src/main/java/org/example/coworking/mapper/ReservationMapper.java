package org.example.coworking.mapper;

import org.example.coworking.dto.ReservationDto;
import org.example.coworking.entity.CoworkingSpace;
import org.example.coworking.entity.Customer;
import org.example.coworking.entity.Reservation;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReservationMapper {

    public Reservation reservationDtoToEntity(ReservationDto dto) {
        Reservation reservation = new Reservation();
        reservation.setStartTime(dto.getStartTime());
        reservation.setEndTime(dto.getEndTime());

        Customer customer = new Customer();
        customer.setId(dto.getCustomerId());
        reservation.setCustomer(customer);

        CoworkingSpace space = new CoworkingSpace();
        space.setId(dto.getCoworkingSpaceId());
        reservation.setCoworkingSpace(space);
        return reservation;
    }

    public ReservationDto reservationEntityToDto(Reservation reservation) {
        LocalDateTime startTime = reservation.getStartTime();
        LocalDateTime endTime = reservation.getEndTime();
        Long customerId = reservation.getCustomer().getId();
        Long coworkingSpaceId = reservation.getCoworkingSpace().getId();
        return new ReservationDto(customerId, startTime, endTime, coworkingSpaceId);
    }

    public List<ReservationDto> reservationEntitiesToDtoList(List<Reservation> reservations) {
        return reservations.stream()
                .map(this::reservationEntityToDto)
                .collect(Collectors.toList());
    }
}
