package com.example.parking.exception;

public class NoAvailableSlotException extends ApiException {
    public NoAvailableSlotException(String message) {
        super(409, "NO_AVAILABLE_SLOT", message);
    }
}
