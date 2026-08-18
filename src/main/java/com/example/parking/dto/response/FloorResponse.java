package com.example.parking.dto.response;

public class FloorResponse {
    private String floorId;
    private int floorNumber;
    private int zoneCount;
    private int slotCount;

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public int getZoneCount() {
        return zoneCount;
    }

    public void setZoneCount(int zoneCount) {
        this.zoneCount = zoneCount;
    }

    public int getSlotCount() {
        return slotCount;
    }

    public void setSlotCount(int slotCount) {
        this.slotCount = slotCount;
    }
}
