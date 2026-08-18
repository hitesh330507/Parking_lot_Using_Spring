package com.example.parking.repository;

import com.example.parking.domain.model.ParkingTicket;

import java.util.Optional;

public interface TicketRepository {
    ParkingTicket save(ParkingTicket ticket);

    Optional<ParkingTicket> findById(String ticketId);
}
