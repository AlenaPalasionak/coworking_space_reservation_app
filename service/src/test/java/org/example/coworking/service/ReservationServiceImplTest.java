package org.example.coworking.service;

import org.example.coworking.entity.CoworkingSpace;
import org.example.coworking.entity.Reservation;
import org.example.coworking.entity.User;
import org.example.coworking.repository.ReservationRepository;
import org.example.coworking.repository.exception.DaoErrorCode;
import org.example.coworking.repository.exception.EntityNotFoundException;
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
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {
    @Mock
    private ReservationRepository reservationDao;
    @Mock
    private TimeLogicValidator timeLogicValidator;
    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Test
    void testAddReservationSuccessfully() throws ReservationTimeException, EntityNotFoundException {
        User customer = new User(2L, "Custer", "321");
        Long coworkingSpaceId = 10L;
        LocalDateTime startTime = LocalDateTime.of(2030, 1, 2, 12, 0);
        LocalDateTime endTime = LocalDateTime.of(2030, 1, 2, 14, 0);

        CoworkingSpace coworkingSpace = new CoworkingSpace();
        coworkingSpace.setId(coworkingSpaceId);
        doNothing().when(timeLogicValidator).validateReservation(startTime, endTime);

        Reservation reservation = new Reservation(customer, startTime, endTime, coworkingSpace);
        reservationService.add(reservation);

        ArgumentCaptor<Reservation> reservationCaptor = ArgumentCaptor.forClass(Reservation.class);
        verify(reservationDao).save(reservationCaptor.capture());

        Reservation capturedReservation = reservationCaptor.getValue();
        assertThat(capturedReservation.getCustomer()).isEqualTo(customer);
        assertThat(capturedReservation.getCoworkingSpace()).isEqualTo(coworkingSpace);
        assertThat(capturedReservation.getStartTime()).isEqualTo(startTime);
        assertThat(capturedReservation.getEndTime()).isEqualTo(endTime);
    }

    @Test
    void testAddReservationWhenCoworkingSpaceNotFound() {
        User customer = new User(2L, "Custer", "321");
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusHours(2);

        Reservation reservation = new Reservation(customer, startTime, endTime, null);

        assertThatThrownBy(() -> reservationService.add(reservation))
                .isInstanceOf(NullPointerException.class);
        verify(reservationDao, never()).save(any());
    }

    @Test
    void testDeleteReservationWhenUserIsOwner() throws EntityNotFoundException, ForbiddenActionException {
        User customer = new User(2L, "Custer", "321");
        Long reservationId = 10L;

        Reservation reservation = Mockito.mock(Reservation.class);
        when(reservationDao.findById(reservationId)).thenReturn(Optional.ofNullable(reservation));
        when(reservation.getCustomer()).thenReturn(customer);

        reservationService.delete(customer, reservationId);

        verify(reservationDao, times(1)).delete(reservation);
    }

    @Test
    void testDeleteReservationWhenUserIsNotOwner() throws EntityNotFoundException {
        User customer = new User(2L, "Custer", "321");
        User anotherCustomer = new User(99L, "Bob", "789");
        Long reservationId = 10L;

        Reservation reservation = Mockito.mock(Reservation.class);
        when(reservationDao.findById(reservationId)).thenReturn(Optional.ofNullable(reservation));
        when(reservation.getCustomer()).thenReturn(customer);

        assertThatThrownBy(() -> reservationService.delete(anotherCustomer, reservationId))
                .isInstanceOf(ForbiddenActionException.class);
        verify(reservationDao, never()).delete(any());
    }

    @Test
    void testGetByIdReturnsReservationWhenExists() throws EntityNotFoundException {
        Long reservationId = 10L;
        Reservation reservation = Mockito.mock(Reservation.class);
        when(reservationDao.findById(reservationId)).thenReturn(Optional.ofNullable(reservation));

        Reservation actualReservation = reservationService.findById(reservationId);

        assertThat(actualReservation).isEqualTo(reservation);
    }

    @Test
    void testGetByIdThrowsExceptionWhenNotFound() throws EntityNotFoundException {
        Long reservationId = 99L;
        when(reservationDao.findById(reservationId))
                .thenThrow(new EntityNotFoundException("Reservation with id: " + reservationId + " is not found.", DaoErrorCode.RESERVATION_IS_NOT_FOUND));

        assertThatThrownBy(() -> reservationService.findById(reservationId))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void testGetAllReservationsByCustomerWithValidCustomer() {
        User customer = new User(1L, "Alice", "123");
        List<Reservation> expectedReservations = List.of(Mockito.mock(Reservation.class));

        when(reservationDao.getAllReservationsByCustomer(customer.getId())).thenReturn(expectedReservations);

        List<Reservation> actualReservations = reservationService.getAllReservationsByCustomer(customer);

        assertThat(actualReservations).isEqualTo(expectedReservations);
    }

    @Test
    void testGetAllReservationsByAdminWithValidAdmin() {
        User admin = new User(1L, "Admin", "admin123");
        List<Reservation> expectedReservations = List.of(Mockito.mock(Reservation.class));

        when(reservationDao.getAllReservationsByAdmin(admin.getId())).thenReturn(expectedReservations);

        List<Reservation> actualReservations = reservationService.getAllReservationsByAdmin(admin);

        assertThat(actualReservations).isEqualTo(expectedReservations);
    }

    @Test
    void testGetAllReservationsByCoworkingReturnsSet() {
        Long coworkingId = 123L;
        Set<Reservation> expectedReservations = Set.of(Mockito.mock(Reservation.class));

        when(reservationDao.getAllReservationsByCoworking(coworkingId)).thenReturn(expectedReservations);

        Set<Reservation> actualReservations = reservationService.getAllReservationsByCoworking(coworkingId);

        assertThat(actualReservations).isEqualTo(expectedReservations);
    }
}
