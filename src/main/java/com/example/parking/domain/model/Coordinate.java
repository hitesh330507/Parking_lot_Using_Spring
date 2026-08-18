package com.example.parking.domain.model;

public record Coordinate(double x, double y) {
    public Coordinate {
        if (Double.isNaN(x) || Double.isNaN(y)) {
            throw new IllegalArgumentException("Coordinates must be numeric");
        }
    }
}
