package org.example.coworking.mapper;

import org.example.coworking.dto.ReservationDto;
import org.example.coworking.entity.CoworkingSpace;
import org.example.coworking.entity.Customer;
import org.example.coworking.entity.Reservation;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ReservationMapperTest {

        private final ReservationMapper reservationMapper = new ReservationMapper();

        @Test
        public void testReservationDtoToEntity() {
            LocalDateTime start = LocalDateTime.of(2025, 4, 23, 10, 0);
            LocalDateTime end = LocalDateTime.of(2025, 4, 23, 12, 0);
            ReservationDto dto = new ReservationDto(1L, start, end, 2L);

            Reservation entity = reservationMapper.reservationDtoToEntity(dto);

            assertThat(entity.getStartTime()).isEqualTo(start);
            assertThat(entity.getEndTime()).isEqualTo(end);
            assertThat(entity.getCustomer().getId()).isEqualTo(1L);
            assertThat(entity.getCoworkingSpace().getId()).isEqualTo(2L);
        }

        @Test
        public void testReservationEntityToDto() {
            Customer customer = new Customer();
            customer.setId(5L);

            CoworkingSpace space = new CoworkingSpace();
            space.setId(8L);

            LocalDateTime start = LocalDateTime.of(2025, 5, 1, 9, 0);
            LocalDateTime end = LocalDateTime.of(2025, 5, 1, 11, 0);

            Reservation reservation = new Reservation();
            reservation.setCustomer(customer);
            reservation.setCoworkingSpace(space);
            reservation.setStartTime(start);
            reservation.setEndTime(end);

            ReservationDto dto = reservationMapper.reservationEntityToDto(reservation);

            assertThat(dto.getCustomerId()).isEqualTo(5L);
            assertThat(dto.getCoworkingSpaceId()).isEqualTo(8L);
            assertThat(dto.getStartTime()).isEqualTo(start);
            assertThat(dto.getEndTime()).isEqualTo(end);
        }

        @Test
        public void testReservationEntitiesToDtoList() {
            Customer customer = new Customer();
            customer.setId(3L);

            CoworkingSpace space = new CoworkingSpace();
            space.setId(4L);

            Reservation reservation1 = new Reservation();
            reservation1.setCustomer(customer);
            reservation1.setCoworkingSpace(space);
            reservation1.setStartTime(LocalDateTime.of(2025, 4, 24, 14, 0));
            reservation1.setEndTime(LocalDateTime.of(2025, 4, 24, 16, 0));

            Reservation reservation2 = new Reservation();
            reservation2.setCustomer(customer);
            reservation2.setCoworkingSpace(space);
            reservation2.setStartTime(LocalDateTime.of(2025, 4, 25, 10, 0));
            reservation2.setEndTime(LocalDateTime.of(2025, 4, 25, 12, 0));

            List<ReservationDto> dtoList = reservationMapper.reservationEntitiesToDtoList(List.of(reservation1, reservation2));

            assertThat(dtoList).hasSize(2);
            assertThat(dtoList.get(0).getStartTime()).isEqualTo(LocalDateTime.of(2025, 4, 24, 14, 0));
            assertThat(dtoList.get(1).getEndTime()).isEqualTo(LocalDateTime.of(2025, 4, 25, 12, 0));
        }
    }
