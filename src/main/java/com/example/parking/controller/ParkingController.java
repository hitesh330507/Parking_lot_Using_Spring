package com.example.parking.controller;

import com.example.parking.domain.enums.SiteStatus;
import com.example.parking.domain.model.Floor;
import com.example.parking.domain.model.ParkingSite;
import com.example.parking.domain.model.ParkingSlot;
import com.example.parking.domain.model.ParkingTicket;
import com.example.parking.dto.request.ParkingEntryRequest;
import com.example.parking.dto.request.ParkingExitRequest;
import com.example.parking.dto.response.AvailabilityResponse;
import com.example.parking.dto.response.ParkingEntryResponse;
import com.example.parking.dto.response.ParkingExitResponse;
import com.example.parking.dto.response.TicketResponse;
import com.example.parking.exception.InvalidExitGateException;
import com.example.parking.exception.InvalidGateException;
import com.example.parking.exception.InvalidSiteStateException;
import com.example.parking.exception.InvalidTimeException;
import com.example.parking.exception.NoAvailableSlotException;
import com.example.parking.mapper.ObjectMapper;
import com.example.parking.service.availability.AvailabilityService;
import com.example.parking.service.fare.FareService;
import com.example.parking.service.parking.ParkingAllocationService;
import com.example.parking.service.site.SiteService;
import com.example.parking.service.ticket.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parking")
public class ParkingController {
    private final SiteService siteService;
    private final TicketService ticketService;
    private final ParkingAllocationService allocationService;
    private final FareService fareService;
    private final AvailabilityService availabilityService;
    private final ObjectMapper mapper;

    public ParkingController(SiteService siteService, TicketService ticketService, ParkingAllocationService allocationService,
                             FareService fareService, AvailabilityService availabilityService, ObjectMapper mapper) {
        this.siteService = siteService;
        this.ticketService = ticketService;
        this.allocationService = allocationService;
        this.fareService = fareService;
        this.availabilityService = availabilityService;
        this.mapper = mapper;
    }

    @PostMapping("/entry")
    public ResponseEntity<ParkingEntryResponse> entry(@Valid @RequestBody ParkingEntryRequest request) {
        ParkingSite site = siteService.getSite(request.getSiteId());
        if (site.getStatus() != SiteStatus.ACTIVE) {
            throw new InvalidSiteStateException("Site is not active");
        }
        boolean gateExists = site.getGates().stream().anyMatch(g -> g.getGateId().equals(request.getEntryGateId()));
        if (!gateExists) {
            throw new InvalidGateException("Gate not found in site");
        }
        ParkingTicket ticket = null;
        for (Floor floor : site.getFloors()) {
            for (ParkingSlot slot : floor.getZones().stream().flatMap(zone -> zone.getSlots().stream()).toList()) {
                if (slot.getVehicleType() == request.getVehicleType() && slot.getStatus() == com.example.parking.domain.enums.SlotStatus.AVAILABLE) {
                    slot.occupy();
                    ticket = ticketService.createTicket(request.getVehicleNumber(), request.getVehicleType(), site.getSiteId(), floor.getFloorId(), slot.getZoneId(), slot.getSlotId(), request.getEntryGateId(), request.getEntryTime());
                    break;
                }
            }
            if (ticket != null) {
                break;
            }
        }
        if (ticket == null) {
            throw new NoAvailableSlotException("No compatible slot available");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toEntryResponse(ticket));
    }

    @PostMapping("/exit")
    public ResponseEntity<ParkingExitResponse> exit(@Valid @RequestBody ParkingExitRequest request) {
        ParkingTicket ticket = ticketService.getTicket(request.getTicketId());
        if (ticket.getStatus() == com.example.parking.domain.enums.TicketStatus.CLOSED) {
            throw new InvalidTimeException("Ticket is already closed");
        }
        if (request.getExitTime().isBefore(ticket.getEntryTime())) {
            throw new InvalidTimeException("Exit time cannot be before entry time");
        }
        ParkingSite site = siteService.getSite(ticket.getSiteId());
        boolean gateExists = site.getGates().stream().anyMatch(g -> g.getGateId().equals(request.getExitGateId()));
        if (!gateExists) {
            throw new InvalidExitGateException("Exit gate not found in site");
        }
        int fee = fareService.calculateFee(ticket.getEntryTime(), request.getExitTime(), site.getHourlyRate());
        for (Floor floor : site.getFloors()) {
            for (ParkingSlot slot : floor.getZones().stream().flatMap(zone -> zone.getSlots().stream()).toList()) {
                if (slot.getSlotId().equals(ticket.getSlotId())) {
                    slot.release();
                    break;
                }
            }
        }
        ticketService.closeTicket(ticket.getTicketId(), request.getExitGateId(), request.getExitTime(), fee);
        return ResponseEntity.ok(mapper.toExitResponse(ticketService.getTicket(ticket.getTicketId())));
    }

    @GetMapping("/tickets/{id}")
    public ResponseEntity<TicketResponse> getTicket(@PathVariable String id) {
        return ResponseEntity.ok(mapper.toTicketResponse(ticketService.getTicket(id)));
    }

    @GetMapping("/sites/{id}/availability")
    public ResponseEntity<AvailabilityResponse> availability(@PathVariable String id) {
        ParkingSite site = siteService.getSite(id);
        return ResponseEntity.ok(availabilityService.calculate(site));
    }
}
