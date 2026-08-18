package com.example.parking.exception;

public class InvalidTimeException extends ApiException {
    public InvalidTimeException(String message) {
        super(400, "INVALID_TIME", message);
    }
}
