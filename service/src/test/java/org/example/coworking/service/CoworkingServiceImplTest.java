package org.example.coworking.service;

import org.example.coworking.dao.CoworkingDao;
import org.example.coworking.dao.exception.CoworkingNotFoundException;
import org.example.coworking.dao.exception.DaoErrorCode;
import org.example.coworking.model.*;
import org.example.coworking.service.exception.ForbiddenActionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoworkingServiceImplTest {
    @Mock
    private CoworkingDao coworkingDao;
    @InjectMocks
    private CoworkingServiceImpl coworkingService;

    @Test
    void testLoad() {
        coworkingService.load();
        verify(coworkingDao, times(1)).load();
    }

    @Test
    void testSave() {
        coworkingService.save();
        verify(coworkingDao, times(1)).save();
    }

    @Test
    void testAdd() {
        User admin = new Admin(1L, "Aden", "123");
        CoworkingType type = CoworkingType.PRIVATE_OFFICE;
        List<Facility> facilities = Arrays.asList(Facility.WIFI, Facility.CONDITIONING, Facility.KITCHEN, Facility.PARKING, Facility.PRINTER);
        double price = 100.0;

        coworkingService.add(admin, price, type, facilities);

        ArgumentCaptor<CoworkingSpace> captor = ArgumentCaptor.forClass(CoworkingSpace.class);
        verify(coworkingDao).add(captor.capture());
        CoworkingSpace capturedSpace = captor.getValue();
        assertThat(capturedSpace)
                .hasFieldOrPropertyWithValue("admin", admin)
                .hasFieldOrPropertyWithValue("price", price)
                .hasFieldOrPropertyWithValue("coworkingType", type)
                .hasFieldOrPropertyWithValue("facilities", facilities);
    }

    @Test
    void testDeleteCoworkingSpaceWhenUserIsOwner() throws ForbiddenActionException, CoworkingNotFoundException {
        Long coworkingSpaceId = 10L;
        User admin = new Admin(1L, "Aden", "123");
        CoworkingSpace coworkingSpace = new CoworkingSpace(admin, 100.0, CoworkingType.CO_LIVING, List.of());
        coworkingSpace.setId(coworkingSpaceId);
        when(coworkingDao.getById(coworkingSpaceId)).thenReturn(coworkingSpace);

        ArgumentCaptor<CoworkingSpace> captor = ArgumentCaptor.forClass(CoworkingSpace.class);
        coworkingService.delete(admin, coworkingSpaceId);

        verify(coworkingDao, times(1)).delete(captor.capture());
        CoworkingSpace capturedCoworkingSpace = captor.getValue();
        assertThat(capturedCoworkingSpace.getId()).isEqualTo(coworkingSpaceId);
    }

    @Test
    void testDeleteCoworkingSpaceWhenUserIsNotOwner() throws CoworkingNotFoundException {
        Long coworkingSpaceId = 10L;
        User admin = new Admin(1L, "Aden", "123");
        CoworkingSpace coworkingSpace = new CoworkingSpace(admin, 100.0, CoworkingType.CO_LIVING, List.of());
        coworkingSpace.setId(coworkingSpaceId);
        User anotherAdmin = new Admin(999L, "Bob", "99");
        when(coworkingDao.getById(coworkingSpaceId)).thenReturn(coworkingSpace);

        assertThatThrownBy(() -> coworkingService.delete(anotherAdmin, coworkingSpaceId))
                .isInstanceOf(ForbiddenActionException.class);

        verify(coworkingDao, never()).delete(coworkingSpace);
    }

    @Test
    void testDeleteCoworkingSpaceWhenCoworkingNotFound() throws CoworkingNotFoundException {
        Long coworkingId = 10L;
        User admin = new Admin(1L, "Aden", "123");
        when(coworkingDao.getById(coworkingId)).thenThrow(new CoworkingNotFoundException("Coworking with id: " + coworkingId + " is not found"
                , DaoErrorCode.COWORKING_IS_NOT_FOUND));

        assertThatThrownBy(() -> coworkingService.delete(admin, coworkingId))
                .isInstanceOf(CoworkingNotFoundException.class);

        verify(coworkingDao, never()).delete(any());
    }

    @Test
    void testGetAllByUserWhenUserIsAdmin() {
        User admin = new Admin(1L, "Aden", "123");
        CoworkingSpace coworking1 = new CoworkingSpace(admin, 100.0, CoworkingType.OPEN_SPACE, List.of());
        CoworkingSpace coworking2 = new CoworkingSpace(admin, 100.0, CoworkingType.PRIVATE_OFFICE, List.of());
        CoworkingSpace coworking3 = new CoworkingSpace(new Admin(2L, "OtherAdmin", "333"), 200.0, CoworkingType.PRIVATE_OFFICE, List.of());
        CoworkingSpace coworking4 = new CoworkingSpace(new Admin(2L, "OtherAdmin", "444"), 200.0, CoworkingType.OPEN_SPACE, List.of());
        when(coworkingDao.getAll()).thenReturn(List.of(coworking1, coworking2, coworking3, coworking4));

        List<CoworkingSpace> actualCoworkingSpaces = coworkingService.getAllByUser(admin);

        assertThat(actualCoworkingSpaces).containsExactly(coworking1, coworking2);
    }

    @Test
    void testGetAllByUserWhenUserIsCustomer() {
        User customer = new Customer(2L, "Custer", "321");
        CoworkingSpace coworking1 = new CoworkingSpace(new Admin(1L, "Aden", "123"), 100.0, CoworkingType.OPEN_SPACE, List.of());
        CoworkingSpace coworking2 = new CoworkingSpace(new Admin(2L, "Bob", "124"), 200.0, CoworkingType.PRIVATE_OFFICE, List.of());
        CoworkingSpace coworking3 = new CoworkingSpace(new Admin(3L, "Rob", "125"), 300.0, CoworkingType.CO_LIVING, List.of());
        CoworkingSpace coworking4 = new CoworkingSpace(new Admin(4L, "Sam", "126"), 400.0, CoworkingType.OPEN_SPACE, List.of());
        when(coworkingDao.getAll()).thenReturn(List.of(coworking1, coworking2, coworking3, coworking4));

        List<CoworkingSpace> actualCoworkingSpaces = coworkingService.getAllByUser(customer);

        assertThat(actualCoworkingSpaces).containsExactly(coworking1, coworking2, coworking3, coworking4);
    }


    @Test
    void testGetByIdReturnsCoworkingSpaceWhenExists() throws CoworkingNotFoundException {
        CoworkingSpace expectedCoworkingSpace = new CoworkingSpace(new Admin(1L, "Aden", "123")
                , 100.0, CoworkingType.OPEN_SPACE, List.of());
        when(coworkingDao.getById(1L)).thenReturn(expectedCoworkingSpace);

        CoworkingSpace actualCoworkingSpace = coworkingService.getById(1L);

        assertThat(actualCoworkingSpace).isEqualTo(expectedCoworkingSpace);
        verify(coworkingDao, times(1)).getById(1L);
    }

    @Test
    void testGetByIdThrowsCoworkingNotFoundExceptionWhenNotFound() throws CoworkingNotFoundException {
        Long coworkingId = 999L;
        when(coworkingDao.getById(coworkingId)).thenThrow(new CoworkingNotFoundException("Coworking with id: " + coworkingId + " is not found"
                , DaoErrorCode.COWORKING_IS_NOT_FOUND));

        assertThatThrownBy(() -> coworkingService.getById(coworkingId))
                .isInstanceOf(CoworkingNotFoundException.class);
        verify(coworkingDao, times(1)).getById(coworkingId);
    }

    @Test
    void testGetCoworkingSpacePeriod() {
        User admin = new Admin(1L, "Aden", "123");
        CoworkingSpace coworkingSpace = new CoworkingSpace(admin, 100.0, CoworkingType.OPEN_SPACE, List.of());
        ReservationPeriod period1 = new ReservationPeriod(LocalDateTime.of(2027, 1, 2, 12, 0)
                , LocalDateTime.of(2028, 1, 2, 12, 0));
        ReservationPeriod period2 = new ReservationPeriod(LocalDateTime.of(2028, 1, 2, 12, 0)
                , LocalDateTime.of(2029, 1, 2, 12, 0));
        coworkingSpace.getReservationsPeriods().add(period1);
        coworkingSpace.getReservationsPeriods().add(period2);

        TreeSet<ReservationPeriod> actualPeriods = coworkingService.getCoworkingSpacePeriod(coworkingSpace);

        assertThat(actualPeriods).containsExactly(period1, period2);
    }
}