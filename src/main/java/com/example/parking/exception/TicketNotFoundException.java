package com.example.parking.exception;

public class TicketNotFoundException extends ApiException {
    public TicketNotFoundException(String ticketId) {
        super(404, "TICKET_NOT_FOUND", "Ticket not found: " + ticketId);
    }
}
