package org.example.coworking.service;

import org.example.coworking.dao.ReservationDao;
import org.example.coworking.dao.exception.DaoErrorCode;
import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.model.*;
import org.example.coworking.service.exception.ForbiddenActionException;
import org.example.coworking.service.exception.ReservationTimeException;
import org.example.coworking.service.validator.TimeLogicValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {
    @Mock
    private ReservationDao reservationDao;
    @Mock
    private CoworkingService coworkingService;
    @Mock
    private TimeLogicValidator timeLogicValidator;
    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Test
    void testAddReservationSuccessfully() throws ReservationTimeException, EntityNotFoundException {
        User customer = new Customer(2L, "Custer", "321");
        Long coworkingSpaceId = 10L;
        LocalDateTime startTime = LocalDateTime.of(2030, 1, 2, 12, 0);
        LocalDateTime endTime = LocalDateTime.of(2030, 1, 2, 14, 0);
        CoworkingSpace coworkingSpace = new CoworkingSpace();

        when(coworkingService.getById(coworkingSpaceId)).thenReturn(coworkingSpace);
        doNothing().when(timeLogicValidator).validateReservation(startTime, endTime);
        when(reservationDao.getAllReservationPeriodsByCoworking(coworkingSpaceId))
                .thenReturn(new TreeSet<>());

        reservationService.add(customer, startTime, endTime, coworkingSpaceId);

        ArgumentCaptor<Reservation> reservationCaptor = ArgumentCaptor.forClass(Reservation.class);
        verify(reservationDao).create(reservationCaptor.capture());

        Reservation capturedReservation = reservationCaptor.getValue();

        assertThat(capturedReservation.getCustomer()).isEqualTo(customer);
        assertThat(capturedReservation.getCoworkingSpace()).isEqualTo(coworkingSpace);
        assertThat(capturedReservation.getPeriod().getStartTime()).isEqualTo(startTime);
        assertThat(capturedReservation.getPeriod().getEndTime()).isEqualTo(endTime);

        verify(coworkingService).getById(coworkingSpaceId);
        verify(timeLogicValidator).validateReservation(startTime, endTime);
        verify(reservationDao).create(any(Reservation.class));
    }

    @Test
    void testAddReservationWhenCoworkingSpaceNotFound() throws EntityNotFoundException {
        User customer = new Customer(2L, "Custer", "321");
        Long coworkingSpaceId = 999L;
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusHours(2);

        when(coworkingService.getById(coworkingSpaceId))
                .thenThrow(new EntityNotFoundException("Coworking with id: " + coworkingSpaceId + " is not found", DaoErrorCode.COWORKING_IS_NOT_FOUND));

        assertThatThrownBy(() -> reservationService.add(customer, startTime, endTime, coworkingSpaceId))
                .isInstanceOf(EntityNotFoundException.class);
        verify(reservationDao, never()).create(any());
    }

    @Test
    void testDeleteReservationWhenUserIsOwner() throws EntityNotFoundException, ForbiddenActionException {
        User customer = new Customer(2L, "Custer", "321");
        Long reservationId = 10L;
        ReservationPeriod period = Mockito.mock(ReservationPeriod.class);
        CoworkingSpace coworkingSpace = Mockito.mock(CoworkingSpace.class);
        Reservation reservation = new Reservation(customer, period, coworkingSpace);
        reservation.setId(reservationId);
        when(reservationDao.getById(reservationId)).thenReturn(reservation);

        reservationService.delete(customer, reservationId);

        verify(reservationDao, times(1)).delete(reservation);
    }

    @Test
    void testDeleteReservationWhenUserIsNotOwner() throws EntityNotFoundException {
        User customer = new Customer(2L, "Custer", "321");
        User anotherCustomer = new Customer(99L, "Bob", "789");
        ReservationPeriod period = Mockito.mock(ReservationPeriod.class);
        CoworkingSpace coworkingSpace = Mockito.mock(CoworkingSpace.class);
        Reservation reservation = new Reservation(customer, period, coworkingSpace);
        Long reservationId = 10L;
        reservation.setId(reservationId);
        when(reservationDao.getById(reservationId)).thenReturn(reservation);

        assertThatThrownBy(() -> reservationService.delete(anotherCustomer, reservationId))
                .isInstanceOf(ForbiddenActionException.class);
        verify(reservationDao, never()).delete(any());
    }

    @Test
    void testGetByIdReturnsReservationWhenExists() throws EntityNotFoundException {
        Long reservationId = 10L;
        Reservation reservation = Mockito.mock(Reservation.class);
        when(reservationDao.getById(reservationId)).thenReturn(reservation);

        Reservation actualReservation = reservationService.getById(reservationId);

        assertThat(actualReservation).isEqualTo(reservation);
    }

    @Test
    void testGetByIdThrowsExceptionWhenNotFound() throws EntityNotFoundException {
        Long reservationId = 99L;
        when(reservationDao.getById(reservationId))
                .thenThrow(new EntityNotFoundException("Reservation with id: " + reservationId + " is not found.", DaoErrorCode.RESERVATION_IS_NOT_FOUND));

        assertThatThrownBy(() -> reservationService.getById(reservationId))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
