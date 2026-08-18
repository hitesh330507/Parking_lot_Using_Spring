package com.example.parking.dto.response;

import java.time.LocalDateTime;

public class TicketResponse {
    private String ticketId;
    private String vehicleNumber;
    private String vehicleType;
    private String siteId;
    private String floorId;
    private String zoneId;
    private String slotId;
    private String entryGateId;
    private LocalDateTime entryTime;
    private String exitGateId;
    private LocalDateTime exitTime;
    private Integer fee;
    private String status;

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

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getEntryGateId() {
        return entryGateId;
    }

    public void setEntryGateId(String entryGateId) {
        this.entryGateId = entryGateId;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public String getExitGateId() {
        return exitGateId;
    }

    public void setExitGateId(String exitGateId) {
        this.exitGateId = exitGateId;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public Integer getFee() {
        return fee;
    }

    public void setFee(Integer fee) {
        this.fee = fee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
