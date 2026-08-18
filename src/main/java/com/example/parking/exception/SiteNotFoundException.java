package com.example.parking.exception;

public class SiteNotFoundException extends ApiException {
    public SiteNotFoundException(String siteId) {
        super(404, "SITE_NOT_FOUND", "Site not found: " + siteId);
    }
}
