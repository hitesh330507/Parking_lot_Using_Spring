package com.example.parking.dto.response;

import com.example.parking.dto.request.CoordinateRequest;

public class GateResponse {
    private String gateId;
    private String type;
    private int floorNumber;
    private CoordinateRequest coordinate;

    public String getGateId() {
        return gateId;
    }

    public void setGateId(String gateId) {
        this.gateId = gateId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public CoordinateRequest getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(CoordinateRequest coordinate) {
        this.coordinate = coordinate;
    }
}
