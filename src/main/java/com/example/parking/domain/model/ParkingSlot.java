package com.example.parking.domain.model;

import com.example.parking.domain.enums.SlotStatus;
import com.example.parking.domain.enums.VehicleType;

public class ParkingSlot {
    private final String slotId;
    private final int slotNumber;
    private final String floorId;
    private final String zoneId;
    private final VehicleType vehicleType;
    private final Coordinate coordinate;
    private volatile SlotStatus status;

    public ParkingSlot(String slotId, int slotNumber, String floorId, String zoneId, VehicleType vehicleType, Coordinate coordinate) {
        this.slotId = slotId;
        this.slotNumber = slotNumber;
        this.floorId = floorId;
        this.zoneId = zoneId;
        this.vehicleType = vehicleType;
        this.coordinate = coordinate;
        this.status = SlotStatus.AVAILABLE;
    }

    public String getSlotId() {
        return slotId;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public String getFloorId() {
        return floorId;
    }

    public String getZoneId() {
        return zoneId;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public SlotStatus getStatus() {
        return status;
    }

    public void occupy() {
        this.status = SlotStatus.OCCUPIED;
    }

    public void release() {
        this.status = SlotStatus.AVAILABLE;
    }
}
