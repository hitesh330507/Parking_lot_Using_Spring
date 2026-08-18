package com.example.parking.dto.response;

public class ParkingExitResponse {
    private String ticketId;
    private String vehicleNumber;
    private int fee;
    private String status;

    public ParkingExitResponse() {}

    public ParkingExitResponse(String ticketId, String vehicleNumber, int fee, String status) {
        this.ticketId = ticketId;
        this.vehicleNumber = vehicleNumber;
        this.fee = fee;
        this.status = status;
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

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
