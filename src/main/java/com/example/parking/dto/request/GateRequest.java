package com.example.parking.dto.request;

import com.example.parking.domain.enums.GateType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class GateRequest {
    @NotBlank(message = "gateId is required")
    private String gateId;

    @NotNull(message = "type is required")
    private GateType type;

    @NotNull(message = "floorNumber is required")
    private Integer floorNumber;

    @NotNull(message = "coordinate is required")
    private CoordinateRequest coordinate;

    public GateRequest() {}

    public GateRequest(String gateId, GateType type, int floorNumber, CoordinateRequest coordinate) {
        this.gateId = gateId;
        this.type = type;
        this.floorNumber = floorNumber;
        this.coordinate = coordinate;
    }

    public String getGateId() {
        return gateId;
    }

    public void setGateId(String gateId) {
        this.gateId = gateId;
    }

    public GateType getType() {
        return gateType();
    }

    public void setType(GateType type) {
        this.type = type;
    }

    public Integer getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }

    public CoordinateRequest getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(CoordinateRequest coordinate) {
        this.coordinate = coordinate;
    }

    private GateType gateType() {
        return type;
    }
}
