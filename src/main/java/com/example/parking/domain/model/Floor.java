package com.example.parking.domain.model;

import java.util.ArrayList;
import java.util.List;

public class Floor {
    private final String floorId;
    private final int floorNumber;
    private final double width;
    private final double height;
    private final List<ParkingZone> zones = new ArrayList<>();
    private final List<Gate> gates = new ArrayList<>();

    public Floor(String floorId, int floorNumber, double width, double height) {
        this.floorId = floorId;
        this.floorNumber = floorNumber;
        this.width = width;
        this.height = height;
    }

    public String getFloorId() {
        return floorId;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public List<ParkingZone> getZones() {
        return zones;
    }

    public List<Gate> getGates() {
        return gates;
    }

    public void addZone(ParkingZone zone) {
        zones.add(zone);
    }

    public void addGate(Gate gate) {
        gates.add(gate);
    }
}
