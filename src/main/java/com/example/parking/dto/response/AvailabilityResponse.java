package com.example.parking.dto.response;

import java.util.Map;

public class AvailabilityResponse {
    private String siteId;
    private int totalSlots;
    private int availableSlots;
    private int occupiedSlots;
    private Map<String, VehicleAvailabilityResponse> byVehicleType;
    private Map<String, FloorAvailabilityResponse> byFloor;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(int totalSlots) {
        this.totalSlots = totalSlots;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
    }

    public int getOccupiedSlots() {
        return occupiedSlots;
    }

    public void setOccupiedSlots(int occupiedSlots) {
        this.occupiedSlots = occupiedSlots;
    }

    public Map<String, VehicleAvailabilityResponse> getByVehicleType() {
        return byVehicleType;
    }

    public void setByVehicleType(Map<String, VehicleAvailabilityResponse> byVehicleType) {
        this.byVehicleType = byVehicleType;
    }

    public Map<String, FloorAvailabilityResponse> getByFloor() {
        return byFloor;
    }

    public void setByFloor(Map<String, FloorAvailabilityResponse> byFloor) {
        this.byFloor = byFloor;
    }
}
