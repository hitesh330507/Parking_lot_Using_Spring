package com.example.parking.domain.model;

import com.example.parking.domain.enums.GateType;

public class Gate {
    private final String gateId;
    private final GateType type;
    private final int floorNumber;
    private final Coordinate coordinate;

    public Gate(String gateId, GateType type, int floorNumber, Coordinate coordinate) {
        this.gateId = gateId;
        this.type = type;
        this.floorNumber = floorNumber;
        this.coordinate = coordinate;
    }

    public String getGateId() {
        return gateId;
    }

    public GateType getType() {
        return type;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }
}
