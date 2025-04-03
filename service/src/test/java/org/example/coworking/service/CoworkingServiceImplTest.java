package org.example.coworking.service;

import org.example.coworking.dao.CoworkingDao;
import org.example.coworking.dao.exception.DaoErrorCode;
import org.example.coworking.dao.exception.EntityNotFoundException;
import org.example.coworking.model.*;
import org.example.coworking.service.exception.ForbiddenActionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

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
    void testAdd() {
        User admin = new Admin(1L, "Aden", "123");
        CoworkingType type = CoworkingType.PRIVATE_OFFICE;
        Set<Facility> facilities = Set.of(
                new Facility(FacilityType.WIFI),
                new Facility(FacilityType.CONDITIONING),
                new Facility(FacilityType.KITCHEN),
                new Facility(FacilityType.PARKING),
                new Facility(FacilityType.PRINTER));

        double price = 100.0;

        coworkingService.add(admin, price, type, facilities);

        ArgumentCaptor<CoworkingSpace> captor = ArgumentCaptor.forClass(CoworkingSpace.class);
        verify(coworkingDao).create(captor.capture());
        CoworkingSpace capturedSpace = captor.getValue();
        assertThat(capturedSpace)
                .hasFieldOrPropertyWithValue("admin", admin)
                .hasFieldOrPropertyWithValue("price", price)
                .hasFieldOrPropertyWithValue("coworkingType", type)
                .hasFieldOrPropertyWithValue("facilities", facilities);
    }

    @Test
    void testDeleteCoworkingSpaceWhenUserIsOwner() throws EntityNotFoundException, ForbiddenActionException {
        Long coworkingSpaceId = 10L;
        User admin = new Admin(1L, "Aden", "123");
        CoworkingSpace coworkingSpace = new CoworkingSpace(admin, 100.0, CoworkingType.CO_LIVING, Set.of());
        coworkingSpace.setId(coworkingSpaceId);
        when(coworkingDao.getById(coworkingSpaceId)).thenReturn(coworkingSpace);

        coworkingService.delete(admin, coworkingSpaceId);

        verify(coworkingDao, times(1)).delete(coworkingSpace);
    }

    @Test
    void testDeleteCoworkingSpaceWhenUserIsNotOwner() throws EntityNotFoundException {
        Long coworkingSpaceId = 10L;
        User admin = new Admin(1L, "Aden", "123");
        CoworkingSpace coworkingSpace = new CoworkingSpace(admin, 100.0, CoworkingType.CO_LIVING, Set.of());
        coworkingSpace.setId(coworkingSpaceId);
        User anotherAdmin = new Admin(999L, "Bob", "99");
        when(coworkingDao.getById(coworkingSpaceId)).thenReturn(coworkingSpace);

        assertThatThrownBy(() -> coworkingService.delete(anotherAdmin, coworkingSpaceId))
                .isInstanceOf(ForbiddenActionException.class);

        verify(coworkingDao, never()).delete(any());
    }

    @Test
    void testDeleteCoworkingSpaceWhenCoworkingNotFound() throws EntityNotFoundException {
        Long coworkingId = 10L;
        User admin = new Admin(1L, "Aden", "123");
        when(coworkingDao.getById(coworkingId)).thenThrow(new EntityNotFoundException("Coworking with id: " + coworkingId + " is not found", DaoErrorCode.COWORKING_IS_NOT_FOUND));

        assertThatThrownBy(() -> coworkingService.delete(admin, coworkingId))
                .isInstanceOf(EntityNotFoundException.class);

        verify(coworkingDao, never()).delete(any());
    }

    @Test
    void testGetAllByUserWhenUserIsAdmin() {
        User admin = new Admin(1L, "Aden", "123");
        CoworkingSpace coworking1 = new CoworkingSpace(admin, 100.0, CoworkingType.OPEN_SPACE, Set.of());
        CoworkingSpace coworking2 = new CoworkingSpace(admin, 100.0, CoworkingType.PRIVATE_OFFICE, Set.of());
        when(coworkingDao.getAllCoworkingSpacesByAdmin(admin.getId())).thenReturn(List.of(coworking1, coworking2));

        List<CoworkingSpace> actualCoworkingSpaces = coworkingService.getAllByUser(admin);

        assertThat(actualCoworkingSpaces).containsExactly(coworking1, coworking2);
    }

    @Test
    void testGetAllByUserWhenUserIsCustomer() {
        User customer = new Customer(2L, "Customer", "321");
        CoworkingSpace coworking1 = new CoworkingSpace(new Admin(1L, "Aden", "123"), 100.0, CoworkingType.OPEN_SPACE, Set.of());
        CoworkingSpace coworking2 = new CoworkingSpace(new Admin(2L, "Bob", "124"), 200.0, CoworkingType.PRIVATE_OFFICE, Set.of());
        when(coworkingDao.getAll()).thenReturn(List.of(coworking1, coworking2));

        List<CoworkingSpace> actualCoworkingSpaces = coworkingService.getAllByUser(customer);

        assertThat(actualCoworkingSpaces).containsExactly(coworking1, coworking2);
    }

    @Test
    void testGetByIdReturnsCoworkingSpaceWhenExists() throws EntityNotFoundException {
        Long coworkingId = 1L;
        CoworkingSpace expectedCoworkingSpace = new CoworkingSpace(new Admin(1L, "Aden", "123"), 100.0, CoworkingType.OPEN_SPACE, Set.of());
        when(coworkingDao.getById(coworkingId)).thenReturn(expectedCoworkingSpace);

        CoworkingSpace actualCoworkingSpace = coworkingService.getById(coworkingId);

        assertThat(actualCoworkingSpace).isEqualTo(expectedCoworkingSpace);
        verify(coworkingDao, times(1)).getById(coworkingId);
    }

    @Test
    void testGetByIdThrowsEntityNotFoundExceptionWhenNotFound() throws EntityNotFoundException {
        Long coworkingId = 999L;
        when(coworkingDao.getById(coworkingId)).thenThrow(new EntityNotFoundException("Coworking with id: " + coworkingId + " is not found", DaoErrorCode.COWORKING_IS_NOT_FOUND));

        assertThatThrownBy(() -> coworkingService.getById(coworkingId))
                .isInstanceOf(EntityNotFoundException.class);
        verify(coworkingDao, times(1)).getById(coworkingId);
    }
}
