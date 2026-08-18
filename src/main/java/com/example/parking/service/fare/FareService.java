package com.example.parking.service.fare;

import com.example.parking.exception.InvalidTimeException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class FareService {
    public int calculateFee(LocalDateTime entryTime, LocalDateTime exitTime, int hourlyRate) {
        if (exitTime.isBefore(entryTime)) {
            throw new InvalidTimeException("Exit time cannot be before entry time");
        }
        long durationMinutes = Duration.between(entryTime, exitTime).toMinutes();
        long chargedHours = (long) Math.ceil(durationMinutes / 60.0);
        return (int) (chargedHours * hourlyRate);
    }
}
