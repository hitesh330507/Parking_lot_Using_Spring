package com.example.parking.exception;

public class InvalidVehicleTypeDistributionException extends ApiException {
    public InvalidVehicleTypeDistributionException(String message) {
        super(400, "INVALID_VEHICLE_DISTRIBUTION", message);
    }
}
