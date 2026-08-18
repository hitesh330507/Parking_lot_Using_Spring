package com.example.parking.service.ticket;

import com.example.parking.domain.enums.TicketStatus;
import com.example.parking.domain.enums.VehicleType;
import com.example.parking.domain.model.ParkingSlot;
import com.example.parking.domain.model.ParkingTicket;
import com.example.parking.exception.TicketAlreadyClosedException;
import com.example.parking.exception.TicketNotFoundException;
import com.example.parking.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public ParkingTicket createTicket(String vehicleNumber, VehicleType vehicleType, String siteId, String floorId,
                                      String zoneId, String slotId, String entryGateId, LocalDateTime entryTime) {
        ParkingTicket ticket = new ParkingTicket(
                "TKT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                vehicleNumber, vehicleType, siteId, floorId, zoneId, slotId, entryGateId, entryTime);
        return ticketRepository.save(ticket);
    }

    public ParkingTicket getTicket(String ticketId) {
        return ticketRepository.findById(ticketId).orElseThrow(() -> new TicketNotFoundException(ticketId));
    }

    public ParkingTicket closeTicket(String ticketId, String exitGateId, LocalDateTime exitTime, int fee) {
        ParkingTicket ticket = getTicket(ticketId);
        if (ticket.getStatus() == TicketStatus.CLOSED) {
            throw new TicketAlreadyClosedException(ticketId);
        }
        ticket.close(exitGateId, exitTime, fee);
        return ticketRepository.save(ticket);
    }
}
