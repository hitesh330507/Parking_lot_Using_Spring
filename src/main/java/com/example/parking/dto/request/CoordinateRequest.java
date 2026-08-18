package com.example.parking.dto.request;

import jakarta.validation.constraints.NotNull;

public class CoordinateRequest {
    @NotNull(message = "x coordinate is required")
    private Double x;

    @NotNull(message = "y coordinate is required")
    private Double y;

    public CoordinateRequest() {}

    public CoordinateRequest(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }
}
