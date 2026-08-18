package com.example.parking.exception;

public class InvalidExitGateException extends ApiException {
    public InvalidExitGateException(String message) {
        super(400, "INVALID_EXIT_GATE", message);
    }
}
