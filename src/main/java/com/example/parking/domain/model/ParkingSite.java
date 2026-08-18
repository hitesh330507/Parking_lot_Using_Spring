package com.example.parking.domain.model;

import com.example.parking.domain.enums.SiteStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParkingSite {
    private final String siteId;
    private final String name;
    private SiteStatus status;
    private final List<Floor> floors = new ArrayList<>();
    private final List<Gate> gates = new ArrayList<>();
    private final Map<String, Integer> vehicleDistribution;
    private final int hourlyRate;
    private final int slotsPerFloor;
    private final int zonesPerFloor;
    private final double floorWidth;
    private final double floorHeight;

    public ParkingSite(String siteId, String name, SiteStatus status, Map<String, Integer> vehicleDistribution,
                       int hourlyRate, int slotsPerFloor, int zonesPerFloor, double floorWidth, double floorHeight) {
        this.siteId = siteId;
        this.name = name;
        this.status = status;
        this.vehicleDistribution = vehicleDistribution;
        this.hourlyRate = hourlyRate;
        this.slotsPerFloor = slotsPerFloor;
        this.zonesPerFloor = zonesPerFloor;
        this.floorWidth = floorWidth;
        this.floorHeight = floorHeight;
    }

    public String getSiteId() {
        return siteId;
    }

    public String getName() {
        return name;
    }

    public SiteStatus getStatus() {
        return status;
    }

    public void setStatus(SiteStatus status) {
        this.status = status;
    }

    public List<Floor> getFloors() {
        return floors;
    }

    public List<Gate> getGates() {
        return gates;
    }

    public Map<String, Integer> getVehicleDistribution() {
        return vehicleDistribution;
    }

    public int getHourlyRate() {
        return hourlyRate;
    }

    public int getSlotsPerFloor() {
        return slotsPerFloor;
    }

    public int getZonesPerFloor() {
        return zonesPerFloor;
    }

    public double getFloorWidth() {
        return floorWidth;
    }

    public double getFloorHeight() {
        return floorHeight;
    }

    public void addFloor(Floor floor) {
        floors.add(floor);
    }

    public void addGate(Gate gate) {
        gates.add(gate);
    }
}
