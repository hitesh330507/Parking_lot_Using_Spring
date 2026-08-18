package com.example.parking.exception;

public class InvalidGateException extends ApiException {
    public InvalidGateException(String message) {
        super(400, "INVALID_GATE", message);
    }
}
