package com.example.parking.service.fare;

import com.example.parking.exception.InvalidTimeException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FareServiceTest {

    private final FareService fareService = new FareService();

    @Test
    void calculateFee_shouldReturnHourlyFee_forExactHours() {
        LocalDateTime entry = LocalDateTime.of(2026, 8, 10, 10, 0);
        LocalDateTime exit = LocalDateTime.of(2026, 8, 10, 12, 0);

        int fee = fareService.calculateFee(entry, exit, 50);

        assertThat(fee).isEqualTo(100);
    }

    @Test
    void calculateFee_shouldRoundUpPartialHour() {
        LocalDateTime entry = LocalDateTime.of(2026, 8, 10, 10, 0);
        LocalDateTime exit = LocalDateTime.of(2026, 8, 10, 11, 15);

        int fee = fareService.calculateFee(entry, exit, 40);

        assertThat(fee).isEqualTo(80);
    }

    @Test
    void calculateFee_shouldThrowInvalidTimeException_whenExitBeforeEntry() {
        LocalDateTime entry = LocalDateTime.of(2026, 8, 10, 12, 0);
        LocalDateTime exit = LocalDateTime.of(2026, 8, 10, 11, 0);

        assertThatThrownBy(() -> fareService.calculateFee(entry, exit, 50))
                .isInstanceOf(InvalidTimeException.class)
                .hasMessageContaining("Exit time cannot be before entry time");
    }
}
