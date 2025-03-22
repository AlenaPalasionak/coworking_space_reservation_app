package org.example.coworking.mapper;

import org.junit.jupiter.api.Test;

import java.time.DateTimeException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ReservationMapperTest {

    private final ReservationMapper mapper = new ReservationMapper();

    @Test
    public void testGetLocalDateTimeValidInput() {
        String yearInput = "2027";
        String monthInput = "3";
        String dayInput = "25";
        String hourInput = "14";
        String minuteInput = "30";

        LocalDateTime expectedDateTime = LocalDateTime.of(2027, 3, 25, 14, 30);
        LocalDateTime actualDateTime = mapper.getLocalDateTime(yearInput, monthInput, dayInput, hourInput, minuteInput);

        assertThat(actualDateTime).isEqualTo(expectedDateTime);
    }

    @Test
    public void testGetLocalDateTimeInValidInput() {
        String yearInput = "25";
        String monthInput = "13";
        String dayInput = "33";
        String hourInput = "25";
        String minuteInput = "60";

        assertThatThrownBy(() -> mapper.getLocalDateTime(yearInput, monthInput, dayInput, hourInput, minuteInput))
                .isInstanceOf(DateTimeException.class);
    }


    @Test
    public void testGetIdValidInput() {
        String idInput = "12345";

        Long expectedId = 12345L;
        Long actualId = mapper.getId(idInput);

        assertThat(actualId).isEqualTo(expectedId);
    }

    @Test
    public void testGetIdInvalidInput() {
        String idInput = "ee11";

        assertThatThrownBy(() -> mapper.getId(idInput))
                .isInstanceOf(NumberFormatException.class);
    }

    @Test
    public void testGetIdEmptyInput() {
        String idInput = "";

        assertThatThrownBy(() -> mapper.getId(idInput))
                .isInstanceOf(NumberFormatException.class);
    }
}
