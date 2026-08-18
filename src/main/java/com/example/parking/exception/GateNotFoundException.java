package com.example.parking.exception;

public class GateNotFoundException extends ApiException {
    public GateNotFoundException(String gateId) {
        super(404, "GATE_NOT_FOUND", "Gate not found: " + gateId);
    }
}
