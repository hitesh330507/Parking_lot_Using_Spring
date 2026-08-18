package com.example.parking.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class CreateSiteRequest {
    @NotBlank(message = "name is required")
    private String name;

    @Positive(message = "numberOfFloors must be positive")
    private int numberOfFloors;

    @Positive(message = "slotsPerFloor must be positive")
    private int slotsPerFloor;

    @Positive(message = "zonesPerFloor must be positive")
    private int zonesPerFloor;

    @Positive(message = "floorWidth must be positive")
    private double floorWidth;

    @Positive(message = "floorHeight must be positive")
    private double floorHeight;

    @Positive(message = "hourlyRate must be positive")
    private int hourlyRate;

    @NotNull(message = "vehicleDistribution is required")
    @Valid
    private VehicleDistributionRequest vehicleDistribution;

    @NotNull(message = "gates are required")
    @Valid
    private List<GateRequest> gates;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfFloors() {
        return numberOfFloors;
    }

    public void setNumberOfFloors(int numberOfFloors) {
        this.numberOfFloors = numberOfFloors;
    }

    public int getSlotsPerFloor() {
        return slotsPerFloor;
    }

    public void setSlotsPerFloor(int slotsPerFloor) {
        this.slotsPerFloor = slotsPerFloor;
    }

    public int getZonesPerFloor() {
        return zonesPerFloor;
    }

    public void setZonesPerFloor(int zonesPerFloor) {
        this.zonesPerFloor = zonesPerFloor;
    }

    public double getFloorWidth() {
        return floorWidth;
    }

    public void setFloorWidth(double floorWidth) {
        this.floorWidth = floorWidth;
    }

    public double getFloorHeight() {
        return floorHeight;
    }

    public void setFloorHeight(double floorHeight) {
        this.floorHeight = floorHeight;
    }

    public int getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(int hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public VehicleDistributionRequest getVehicleDistribution() {
        return vehicleDistribution;
    }

    public void setVehicleDistribution(VehicleDistributionRequest vehicleDistribution) {
        this.vehicleDistribution = vehicleDistribution;
    }

    public List<GateRequest> getGates() {
        return gates;
    }

    public void setGates(List<GateRequest> gates) {
        this.gates = gates;
    }
}
