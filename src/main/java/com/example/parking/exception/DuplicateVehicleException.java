package com.example.parking.exception;

public class DuplicateVehicleException extends ApiException {
    public DuplicateVehicleException(String vehicleNumber) {
        super(409, "DUPLICATE_VEHICLE", "Vehicle is already parked: " + vehicleNumber);
    }
}
