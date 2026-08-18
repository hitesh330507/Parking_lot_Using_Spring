package com.example.parking.dto.response;

import java.util.List;
import java.util.Map;

public class SiteResponse {
    private String siteId;
    private String name;
    private String status;
    private int numberOfFloors;
    private int slotsPerFloor;
    private int zonesPerFloor;
    private double floorWidth;
    private double floorHeight;
    private int hourlyRate;
    private Map<String, Integer> vehicleDistribution;
    private List<FloorResponse> floors;
    private List<GateResponse> gates;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Map<String, Integer> getVehicleDistribution() {
        return vehicleDistribution;
    }

    public void setVehicleDistribution(Map<String, Integer> vehicleDistribution) {
        this.vehicleDistribution = vehicleDistribution;
    }

    public List<FloorResponse> getFloors() {
        return floors;
    }

    public void setFloors(List<FloorResponse> floors) {
        this.floors = floors;
    }

    public List<GateResponse> getGates() {
        return gates;
    }

    public void setGates(List<GateResponse> gates) {
        this.gates = gates;
    }
}
