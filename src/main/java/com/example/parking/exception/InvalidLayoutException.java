package com.example.parking.exception;

public class InvalidLayoutException extends ApiException {
    public InvalidLayoutException(String message) {
        super(400, "INVALID_LAYOUT", message);
    }
}
