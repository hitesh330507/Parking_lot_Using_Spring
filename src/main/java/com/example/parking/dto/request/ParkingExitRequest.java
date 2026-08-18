package com.example.parking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class ParkingExitRequest {
    @NotBlank(message = "ticketId is required")
    private String ticketId;

    @NotBlank(message = "exitGateId is required")
    private String exitGateId;

    @NotNull(message = "exitTime is required")
    private LocalDateTime exitTime;

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getExitGateId() {
        return exitGateId;
    }

    public void setExitGateId(String exitGateId) {
        this.exitGateId = exitGateId;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }
}
