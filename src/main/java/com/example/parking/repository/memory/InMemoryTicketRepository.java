package com.example.parking.repository.memory;

import com.example.parking.domain.model.ParkingTicket;
import com.example.parking.repository.TicketRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryTicketRepository implements TicketRepository {
    private final ConcurrentMap<String, ParkingTicket> stores = new ConcurrentHashMap<>();

    @Override
    public ParkingTicket save(ParkingTicket ticket) {
        stores.put(ticket.getTicketId(), ticket);
        return ticket;
    }

    @Override
    public Optional<ParkingTicket> findById(String ticketId) {
        return Optional.ofNullable(stores.get(ticketId));
    }
}
