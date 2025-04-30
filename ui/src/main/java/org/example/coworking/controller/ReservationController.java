package org.example.coworking.controller;

import jakarta.validation.Valid;
import org.example.coworking.dto.ReservationDto;
import org.example.coworking.entity.Reservation;
import org.example.coworking.entity.User;
import org.example.coworking.mapper.ReservationMapper;
import org.example.coworking.mapper.UserMapper;
import org.example.coworking.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReservationController {
    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;
    private final UserMapper userMapper;

    @Autowired
    public ReservationController(ReservationService reservationService, ReservationMapper reservationMapper, UserMapper userMapper) {
        this.reservationService = reservationService;
        this.reservationMapper = reservationMapper;
        this.userMapper = userMapper;
    }

    @PostMapping("/reservations")//only for CUSTOMER
    public ResponseEntity<Void> add(@Valid @RequestBody ReservationDto reservationDto) {
        Reservation reservation = reservationMapper.reservationDtoToEntity(reservationDto);
        reservationService.add(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("reservations/{id}")//only for CUSTOMER
    public ResponseEntity<Void> delete(@Valid @PathVariable Long id, @Valid @RequestParam Long customerId) {
        User customer = userMapper.getUserEntity(customerId);
        reservationService.delete(customer, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservations/admin")//only for ADMIN
    public ResponseEntity<List<ReservationDto>> getAllReservationsByAdmin(@RequestParam Long adminId) {
        User admin = userMapper.getUserEntity(adminId);
        List<Reservation> reservations = reservationService.getAllReservationsByAdmin(admin);
        List<ReservationDto> reservationDtoList = reservationMapper.reservationEntitiesToDtoList(reservations);
        return ResponseEntity.ok(reservationDtoList);
    }

    @GetMapping("/reservations/customer")//only for CUSTOMER
    public ResponseEntity<List<ReservationDto>> getAllReservationsByCustomer(@RequestParam Long customerId) {
        User customer = userMapper.getUserEntity(customerId);
        List<Reservation> reservations = reservationService.getAllReservationsByCustomer(customer);
        List<ReservationDto> reservationDtoList = reservationMapper.reservationEntitiesToDtoList(reservations);
        return ResponseEntity.ok(reservationDtoList);
    }
}
