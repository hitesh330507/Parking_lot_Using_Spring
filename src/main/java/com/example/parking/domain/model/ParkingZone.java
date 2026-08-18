package com.example.parking.domain.model;

import java.util.ArrayList;
import java.util.List;

public class ParkingZone {
    private final String zoneId;
    private final int zoneNumber;
    private final Coordinate topLeft;
    private final Coordinate bottomRight;
    private final List<ParkingSlot> slots = new ArrayList<>();

    public ParkingZone(String zoneId, int zoneNumber, Coordinate topLeft, Coordinate bottomRight) {
        this.zoneId = zoneId;
        this.zoneNumber = zoneNumber;
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public String getZoneId() {
        return zoneId;
    }

    public int getZoneNumber() {
        return zoneNumber;
    }

    public Coordinate getTopLeft() {
        return topLeft;
    }

    public Coordinate getBottomRight() {
        return bottomRight;
    }

    public List<ParkingSlot> getSlots() {
        return slots;
    }

    public void addSlot(ParkingSlot slot) {
        slots.add(slot);
    }
}
