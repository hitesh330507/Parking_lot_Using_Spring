package com.example.parking.exception;

public class InvalidSiteStateException extends ApiException {
    public InvalidSiteStateException(String message) {
        super(409, "INVALID_SITE_STATE", message);
    }
}
