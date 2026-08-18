package com.example.parking.dto.response;

public class ParkingEntryResponse {
    private String ticketId;
    private String vehicleNumber;
    private String vehicleType;
    private String slotId;
    private String siteId;

    public ParkingEntryResponse() {}

    public ParkingEntryResponse(String ticketId, String vehicleNumber, String vehicleType, String slotId, String siteId) {
        this.ticketId = ticketId;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.slotId = slotId;
        this.siteId = siteId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
}
