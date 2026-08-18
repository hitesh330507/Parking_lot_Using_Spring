package com.example.parking.domain.model;

import com.example.parking.domain.enums.TicketStatus;
import com.example.parking.domain.enums.VehicleType;

import java.time.LocalDateTime;

public class ParkingTicket {
    private final String ticketId;
    private final String vehicleNumber;
    private final VehicleType vehicleType;
    private final String siteId;
    private final String floorId;
    private final String zoneId;
    private final String slotId;
    private final String entryGateId;
    private final LocalDateTime entryTime;
    private String exitGateId;
    private LocalDateTime exitTime;
    private Integer fee;
    private TicketStatus status;

    public ParkingTicket(String ticketId, String vehicleNumber, VehicleType vehicleType, String siteId,
                         String floorId, String zoneId, String slotId, String entryGateId, LocalDateTime entryTime) {
        this.ticketId = ticketId;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.siteId = siteId;
        this.floorId = floorId;
        this.zoneId = zoneId;
        this.slotId = slotId;
        this.entryGateId = entryGateId;
        this.entryTime = entryTime;
        this.status = TicketStatus.ACTIVE;
    }

    public String getTicketId() {
        return ticketId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public String getSiteId() {
        return siteId;
    }

    public String getFloorId() {
        return floorId;
    }

    public String getZoneId() {
        return zoneId;
    }

    public String getSlotId() {
        return slotId;
    }

    public String getEntryGateId() {
        return entryGateId;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public String getExitGateId() {
        return exitGateId;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public Integer getFee() {
        return fee;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void close(String exitGateId, LocalDateTime exitTime, int fee) {
        this.exitGateId = exitGateId;
        this.exitTime = exitTime;
        this.fee = fee;
        this.status = TicketStatus.CLOSED;
    }
}
