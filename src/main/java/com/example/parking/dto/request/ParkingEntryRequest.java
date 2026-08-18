package com.example.parking.dto.request;

import com.example.parking.domain.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class ParkingEntryRequest {
    @NotBlank(message = "siteId is required")
    private String siteId;

    @NotBlank(message = "entryGateId is required")
    private String entryGateId;

    @NotBlank(message = "vehicleNumber is required")
    private String vehicleNumber;

    @NotNull(message = "vehicleType is required")
    private VehicleType vehicleType;

    @NotNull(message = "entryTime is required")
    private LocalDateTime entryTime;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getEntryGateId() {
        return entryGateId;
    }

    public void setEntryGateId(String entryGateId) {
        this.entryGateId = entryGateId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }
}
