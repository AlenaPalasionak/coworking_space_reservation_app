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
import java.util.List;
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
    ReservationServiceImpl reservationService;

    @Test
    void testAddReservationSuccessfully() throws ReservationTimeException, EntityNotFoundException {
        User customer = new Customer(2L, "Custer", "321");
        Long coworkingSpaceId = 10L;
        LocalDateTime startTime = LocalDateTime.of(2030, 1, 2, 12, 0);
        LocalDateTime endTime = LocalDateTime.of(2030, 1, 2, 14, 0); // Исправлено: разумный интервал
        CoworkingSpace coworkingSpace = new CoworkingSpace();
        when(coworkingService.getById(coworkingSpaceId)).thenReturn(coworkingSpace);
        doNothing().when(timeLogicValidator).validateReservation(startTime, endTime);
        when(coworkingService.getCoworkingSpacePeriod(coworkingSpace))
                .thenReturn(new TreeSet<>()); // Нет пересекающихся периодов

        reservationService.add(customer, startTime, endTime, coworkingSpaceId);


        ArgumentCaptor<Reservation> reservationCaptor = ArgumentCaptor.forClass(Reservation.class);
        verify(reservationDao).add(reservationCaptor.capture());

        Reservation capturedReservation = reservationCaptor.getValue();

        assertThat(capturedReservation.getCustomer()).isEqualTo(customer);
        assertThat(capturedReservation.getCoworkingSpace()).isEqualTo(coworkingSpace);
        assertThat(capturedReservation.getPeriod().getStartTime()).isEqualTo(startTime);
        assertThat(capturedReservation.getPeriod().getEndTime()).isEqualTo(endTime);
        verify(coworkingService).getById(coworkingSpaceId);
        verify(timeLogicValidator).validateReservation(startTime, endTime);
        verify(coworkingService).getCoworkingSpacePeriod(coworkingSpace);
        verify(reservationDao).add(any(Reservation.class));
    }

    @Test
    void testAddReservationWhenCoworkingSpaceNotFound() throws EntityNotFoundException {
        User customer = new Customer(2L, "Custer", "321");
        Long coworkingSpaceId = 999L;
        LocalDateTime startTime = Mockito.mock(LocalDateTime.class);
        LocalDateTime endTime = Mockito.mock(LocalDateTime.class);

        when(coworkingService.getById(coworkingSpaceId)).thenThrow(new EntityNotFoundException("Coworking with id: "
                + coworkingSpaceId + " is not found", DaoErrorCode.COWORKING_IS_NOT_FOUND));

        assertThatThrownBy(() -> reservationService.add(customer, startTime, endTime, coworkingSpaceId))
                .isInstanceOf(EntityNotFoundException.class);
        verify(reservationDao, never()).add(any());
    }

    @Test
    void testAddReservationWhenTimeOverlaps() throws EntityNotFoundException, ReservationTimeException {
        User customer = new Customer(2L, "Custer", "321");
        Long coworkingSpaceId = 10L;
        LocalDateTime startTime = LocalDateTime.of(2030, 1, 2, 12, 0);
        LocalDateTime endTime = LocalDateTime.of(2031, 1, 2, 12, 0);
        CoworkingSpace coworkingSpace = new CoworkingSpace();
        TreeSet<ReservationPeriod> existingPeriods = new TreeSet<>();
        existingPeriods.add(new ReservationPeriod(startTime.minusHours(1), endTime.plusHours(1)));

        when(coworkingService.getById(coworkingSpaceId)).thenReturn(coworkingSpace);
        doNothing().when(timeLogicValidator).validateReservation(startTime, endTime);
        when(coworkingService.getCoworkingSpacePeriod(coworkingSpace)).thenReturn(existingPeriods);

        assertThatThrownBy(() -> reservationService.add(customer, startTime, endTime, coworkingSpaceId))
                .isInstanceOf(ReservationTimeException.class);

        verify(reservationDao, never()).add(any(Reservation.class));
        verify(reservationDao, never()).addPeriodToCoworking(any(ReservationPeriod.class), any(CoworkingSpace.class));

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

        ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);
        reservationService.delete(customer, reservationId);

        verify(reservationDao, times(1)).getById(reservationId);
        verify(reservationDao, times(1)).delete(captor.capture());

        Reservation capturedReservation = captor.getValue();
        assertThat(capturedReservation.getId()).isEqualTo(reservationId);
        assertThat(capturedReservation.getCustomer()).isEqualTo(customer);

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

        verify(reservationDao, never()).delete(any(Reservation.class));
    }


    @Test
    void testDeleteReservationWhenCoworkingNotFound() throws EntityNotFoundException {
        Long reservationId = 99L;
        User customer = new Customer(2L, "Custer", "321");
        when(reservationDao.getById(reservationId)).thenThrow(new EntityNotFoundException("Coworking with id: " + reservationId + " is not found"
                , DaoErrorCode.COWORKING_IS_NOT_FOUND));

        assertThatThrownBy(() -> reservationService.delete(customer, reservationId))
                .isInstanceOf(EntityNotFoundException.class);

        verify(reservationDao, never()).delete(any());
    }

    @Test
    void getAllByUserWhenUserIsCustomer() {
        User customer = new Customer(2L, "Custer", "321");
        User anotherCustomer = new Customer(99L, "Bob", "789");
        Reservation reservation1 = new Reservation(customer, Mockito.mock(ReservationPeriod.class), Mockito.mock(CoworkingSpace.class));
        Reservation reservation2 = new Reservation(customer, Mockito.mock(ReservationPeriod.class), Mockito.mock(CoworkingSpace.class));
        Reservation reservationOfAnotherCustomer1 = new Reservation(anotherCustomer, Mockito.mock(ReservationPeriod.class), Mockito.mock(CoworkingSpace.class));
        Reservation reservationOfAnotherCustomer2 = new Reservation(anotherCustomer, Mockito.mock(ReservationPeriod.class), Mockito.mock(CoworkingSpace.class));
        when(reservationDao.getAll()).thenReturn(List.of(reservation1, reservation2, reservationOfAnotherCustomer1, reservationOfAnotherCustomer2));

        List<Reservation> customerReservations = reservationService.getAllByUser(customer);

        assertThat(customerReservations).isEqualTo(List.of(reservation1, reservation2));
    }

    @Test
    void getAllByUserWhenUserIsAdmin() {
        User admin = new Admin(2L, "Aden", "123");
        User anotherAdmin = new Admin(99L, "Tom", "789");
        CoworkingSpace coworkingSpace = new CoworkingSpace(admin, 100.0, CoworkingType.CO_LIVING, List.of());
        CoworkingSpace coworkingSpaceOfAnotherAdmin = new CoworkingSpace(anotherAdmin, 100.0, CoworkingType.CO_LIVING, List.of());
        Reservation reservation1 = new Reservation(Mockito.mock(User.class), Mockito.mock(ReservationPeriod.class), coworkingSpace);
        Reservation reservation2 = new Reservation(Mockito.mock(User.class), Mockito.mock(ReservationPeriod.class), coworkingSpace);
        Reservation reservationOfAnotherAdmin1 = new Reservation(Mockito.mock(User.class), Mockito.mock(ReservationPeriod.class), coworkingSpaceOfAnotherAdmin);
        Reservation reservationOfAnotherAdmin2 = new Reservation(Mockito.mock(User.class), Mockito.mock(ReservationPeriod.class), coworkingSpaceOfAnotherAdmin);
        when(reservationDao.getAll()).thenReturn(List.of(reservation1, reservation2, reservationOfAnotherAdmin1, reservationOfAnotherAdmin2));

        List<Reservation> adminReservations = reservationService.getAllByUser(admin);

        assertThat(adminReservations).isEqualTo(List.of(reservation1, reservation2));
    }

    @Test
    void testGetByIdReturnsReservationWhenExists() throws EntityNotFoundException {
        Long reservationId = 10L;
        Reservation reservation = new Reservation(Mockito.mock(Customer.class), Mockito.mock(ReservationPeriod.class), Mockito.mock(CoworkingSpace.class));
        reservation.setId(reservationId);
        when(reservationDao.getById(reservationId)).thenReturn(reservation);

        Reservation actualReservation = reservationService.getById(reservationId);

        assertThat(actualReservation).isEqualTo(reservation);
    }

    @Test
    void testGetByIdThrowsReservationNotFoundExceptionWhenNotFound() throws EntityNotFoundException {
        Long reservationId = 99L;
        Reservation reservation = new Reservation(Mockito.mock(Customer.class), Mockito.mock(ReservationPeriod.class), Mockito.mock(CoworkingSpace.class));
        reservation.setId(reservationId);

        when(reservationDao.getById(reservationId)).thenThrow(new EntityNotFoundException
                ("Reservation with id: " + reservation.getId() + " is not found. ", DaoErrorCode.RESERVATION_IS_NOT_FOUND));

        assertThatThrownBy(() -> reservationService.getById(reservationId))
                .isInstanceOf(EntityNotFoundException.class);
    }
}