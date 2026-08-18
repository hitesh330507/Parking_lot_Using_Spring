package com.example.parking.exception;

public class TicketAlreadyClosedException extends ApiException {
    public TicketAlreadyClosedException(String ticketId) {
        super(409, "TICKET_ALREADY_CLOSED", "Ticket is already closed: " + ticketId);
    }
}
