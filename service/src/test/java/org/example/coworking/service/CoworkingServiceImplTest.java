package org.example.coworking.service;

import org.example.coworking.entity.*;
import org.example.coworking.repository.CoworkingSpaceRepository;
import org.example.coworking.repository.exception.DaoErrorCode;
import org.example.coworking.repository.exception.EntityNotFoundException;
import org.example.coworking.service.exception.ForbiddenActionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoworkingServiceImplTest {
    @Mock
    private CoworkingSpaceRepository coworkingRepository;

    @InjectMocks
    private CoworkingServiceImpl coworkingService;

    @Test
    void testAdd() {
        User admin = new User(1L, "Aden", "123");
        CoworkingType type = CoworkingType.PRIVATE_OFFICE;
        Set<Facility> facilities = Set.of(
                Facility.WIFI,
                Facility.CONDITIONING,
                Facility.KITCHEN,
                Facility.PARKING,
                Facility.PRINTER);

        double price = 100.0;


        coworkingService.save(new CoworkingSpace(admin, price, type, facilities));

        ArgumentCaptor<CoworkingSpace> captor = ArgumentCaptor.forClass(CoworkingSpace.class);
        verify(coworkingRepository).save(captor.capture());
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
        User admin = new User(1L, "Aden", "123");
        CoworkingSpace coworkingSpace = new CoworkingSpace(admin, 100.0, CoworkingType.CO_LIVING, Set.of());
        coworkingSpace.setId(coworkingSpaceId);
        when(coworkingRepository.findById(coworkingSpaceId)).thenReturn(Optional.of(coworkingSpace));

        coworkingService.delete(admin, coworkingSpaceId);

        verify(coworkingRepository, times(1)).delete(coworkingSpace);
    }

    @Test
    void testDeleteCoworkingSpaceWhenUserIsNotOwner() throws EntityNotFoundException {
        Long coworkingSpaceId = 10L;
        User admin = new User(1L, "Aden", "123");
        User anotherAdmin = new User(999L, "Bob", "99");

        CoworkingSpace coworkingSpace = Mockito.mock(CoworkingSpace.class);
        when(coworkingRepository.findById(coworkingSpaceId)).thenReturn(Optional.ofNullable(coworkingSpace));
        when(coworkingSpace.getAdmin()).thenReturn(admin);

        assertThatThrownBy(() -> coworkingService.delete(anotherAdmin, coworkingSpaceId))
                .isInstanceOf(ForbiddenActionException.class);

        verify(coworkingRepository, never()).delete(any());
    }

    @Test
    void testDeleteCoworkingSpaceWhenCoworkingNotFound() throws EntityNotFoundException {
        Long coworkingId = 10L;
        User admin = new User(1L, "Aden", "123");

        when(coworkingRepository.findById(coworkingId))
                .thenThrow(new EntityNotFoundException("Coworking with id: " + coworkingId + " is not found", DaoErrorCode.COWORKING_IS_NOT_FOUND));

        assertThatThrownBy(() -> coworkingService.delete(admin, coworkingId))
                .isInstanceOf(EntityNotFoundException.class);

        verify(coworkingRepository, never()).delete(any());
    }

    @Test
    void testGetByIdReturnsCoworkingSpaceWhenExists() throws EntityNotFoundException {
        Long coworkingId = 1L;
        CoworkingSpace expectedCoworkingSpace = new CoworkingSpace(new User(1L, "Aden", "123"), 100.0, CoworkingType.OPEN_SPACE, Set.of());
        when(coworkingRepository.findById(coworkingId)).thenReturn(Optional.of(expectedCoworkingSpace));

        CoworkingSpace actualCoworkingSpace = coworkingService.findById(coworkingId);

        assertThat(actualCoworkingSpace).isEqualTo(expectedCoworkingSpace);
        verify(coworkingRepository, times(1)).findById(coworkingId);
    }

    @Test
    void testFindByIdThrowsEntityNotFoundExceptionWhenNotFound() throws EntityNotFoundException {
        Long coworkingId = 999L;
        when(coworkingRepository.findById(coworkingId)).thenThrow(new EntityNotFoundException("Coworking with id: " + coworkingId + " is not found", DaoErrorCode.COWORKING_IS_NOT_FOUND));

        assertThatThrownBy(() -> coworkingService.findById(coworkingId))
                .isInstanceOf(EntityNotFoundException.class);
        verify(coworkingRepository, times(1)).findById(coworkingId);
    }

    @Test
    void testGetAllReturnsAllCoworkingSpaces() {
        List<CoworkingSpace> expectedList = List.of(
                new CoworkingSpace(new User(1L, "Admin1", "123"), 100.0, CoworkingType.OPEN_SPACE, Set.of()),
                new CoworkingSpace(new User(2L, "Admin2", "456"), 200.0, CoworkingType.PRIVATE_OFFICE, Set.of())
        );

        when(coworkingRepository.findAll()).thenReturn(expectedList);

        List<CoworkingSpace> actualList = coworkingService.findAll();

        assertThat(actualList).isEqualTo(expectedList);
        verify(coworkingRepository, times(1)).findAll();
    }

    @Test
    void testGetAllByAdminReturnsCorrectList() {
        User admin = new User(1L, "Admin", "pass");
        List<CoworkingSpace> expectedList = List.of(
                new CoworkingSpace(admin, 100.0, CoworkingType.OPEN_SPACE, Set.of())
        );

        when(coworkingRepository.getAllCoworkingSpacesByAdmin(admin.getId())).thenReturn(expectedList);

        List<CoworkingSpace> actualList = coworkingService.getAllByAdmin(admin);

        assertThat(actualList).isEqualTo(expectedList);
        verify(coworkingRepository, times(1)).getAllCoworkingSpacesByAdmin(admin.getId());
    }
}
