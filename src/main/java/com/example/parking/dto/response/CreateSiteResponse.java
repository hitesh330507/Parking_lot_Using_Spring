package com.example.parking.dto.response;

public class CreateSiteResponse {
    private String siteId;
    private String name;
    private String status;

    public CreateSiteResponse() {}

    public CreateSiteResponse(String siteId, String name, String status) {
        this.siteId = siteId;
        this.name = name;
        this.status = status;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
