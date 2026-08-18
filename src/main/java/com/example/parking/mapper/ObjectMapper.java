package com.example.parking.mapper;

import com.example.parking.domain.enums.SiteStatus;
import com.example.parking.domain.model.*;
import com.example.parking.dto.request.CoordinateRequest;
import com.example.parking.dto.request.CreateSiteRequest;
import com.example.parking.dto.response.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ObjectMapper {
    private final com.fasterxml.jackson.databind.ObjectMapper jsonMapper = new com.fasterxml.jackson.databind.ObjectMapper()
            .registerModule(new JavaTimeModule());

    public <T> T readValue(String content, Class<T> clazz) throws JsonProcessingException {
        return jsonMapper.readValue(content, clazz);
    }

    public ParkingSite toDomain(CreateSiteRequest request, String siteId) {
        Map<String, Integer> distribution = request.getVehicleDistribution().toVehicleTypeMap().entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().name(), Map.Entry::getValue, (a,b) -> a, LinkedHashMap::new));
        return new ParkingSite(siteId, request.getName(), SiteStatus.DRAFT, distribution, request.getHourlyRate(),
                request.getSlotsPerFloor(), request.getZonesPerFloor(), request.getFloorWidth(), request.getFloorHeight());
    }

    public CreateSiteResponse toCreateSiteResponse(ParkingSite site) {
        return new CreateSiteResponse(site.getSiteId(), site.getName(), site.getStatus().name());
    }

    public SiteResponse toSiteResponse(ParkingSite site) {
        SiteResponse response = new SiteResponse();
        response.setSiteId(site.getSiteId());
        response.setName(site.getName());
        response.setStatus(site.getStatus().name());
        response.setNumberOfFloors(site.getFloors().size());
        response.setSlotsPerFloor(site.getSlotsPerFloor());
        response.setZonesPerFloor(site.getZonesPerFloor());
        response.setFloorWidth(site.getFloorWidth());
        response.setFloorHeight(site.getFloorHeight());
        response.setHourlyRate(site.getHourlyRate());
        response.setVehicleDistribution(new LinkedHashMap<>(site.getVehicleDistribution()));
        response.setFloors(site.getFloors().stream().map(this::toFloorResponse).toList());
        response.setGates(site.getGates().stream().map(this::toGateResponse).toList());
        return response;
    }

    private FloorResponse toFloorResponse(Floor floor) {
        FloorResponse response = new FloorResponse();
        response.setFloorId(floor.getFloorId());
        response.setFloorNumber(floor.getFloorNumber());
        response.setZoneCount(floor.getZones().size());
        response.setSlotCount(floor.getZones().stream().mapToInt(z -> z.getSlots().size()).sum());
        return response;
    }

    private GateResponse toGateResponse(Gate gate) {
        GateResponse response = new GateResponse();
        response.setGateId(gate.getGateId());
        response.setType(gate.getType().name());
        response.setFloorNumber(gate.getFloorNumber());
        response.setCoordinate(toCoordinateRequest(gate.getCoordinate()));
        return response;
    }

    private CoordinateRequest toCoordinateRequest(Coordinate coordinate) {
        return new CoordinateRequest(coordinate.x(), coordinate.y());
    }

    public ParkingEntryResponse toEntryResponse(ParkingTicket ticket) {
        return new ParkingEntryResponse(ticket.getTicketId(), ticket.getVehicleNumber(), ticket.getVehicleType().name(), ticket.getSlotId(), ticket.getSiteId());
    }

    public ParkingExitResponse toExitResponse(ParkingTicket ticket) {
        return new ParkingExitResponse(ticket.getTicketId(), ticket.getVehicleNumber(), ticket.getFee() == null ? 0 : ticket.getFee(), ticket.getStatus().name());
    }

    public TicketResponse toTicketResponse(ParkingTicket ticket) {
        TicketResponse response = new TicketResponse();
        response.setTicketId(ticket.getTicketId());
        response.setVehicleNumber(ticket.getVehicleNumber());
        response.setVehicleType(ticket.getVehicleType().name());
        response.setSiteId(ticket.getSiteId());
        response.setFloorId(ticket.getFloorId());
        response.setZoneId(ticket.getZoneId());
        response.setSlotId(ticket.getSlotId());
        response.setEntryGateId(ticket.getEntryGateId());
        response.setEntryTime(ticket.getEntryTime());
        response.setExitGateId(ticket.getExitGateId());
        response.setExitTime(ticket.getExitTime());
        response.setFee(ticket.getFee());
        response.setStatus(ticket.getStatus().name());
        return response;
    }

    public AvailabilityResponse toAvailabilityResponse(ParkingSite site, int totalSlots, int availableSlots, int occupiedSlots,
                                                      Map<String, VehicleAvailabilityResponse> byVehicleType,
                                                      Map<String, FloorAvailabilityResponse> byFloor) {
        AvailabilityResponse response = new AvailabilityResponse();
        response.setSiteId(site.getSiteId());
        response.setTotalSlots(totalSlots);
        response.setAvailableSlots(availableSlots);
        response.setOccupiedSlots(occupiedSlots);
        response.setByVehicleType(byVehicleType);
        response.setByFloor(byFloor);
        return response;
    }
}
