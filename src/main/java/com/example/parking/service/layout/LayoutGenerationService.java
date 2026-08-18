package com.example.parking.service.layout;

import com.example.parking.domain.enums.GateType;
import com.example.parking.domain.enums.VehicleType;
import com.example.parking.domain.model.Coordinate;
import com.example.parking.domain.model.Floor;
import com.example.parking.domain.model.Gate;
import com.example.parking.domain.model.ParkingSite;
import com.example.parking.domain.model.ParkingSlot;
import com.example.parking.domain.model.ParkingZone;
import com.example.parking.dto.request.CreateSiteRequest;
import com.example.parking.dto.request.GateRequest;
import com.example.parking.exception.InvalidLayoutException;
import com.example.parking.exception.InvalidVehicleTypeDistributionException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class LayoutGenerationService {

    public void generateLayout(ParkingSite site, CreateSiteRequest request) {
        validateVehicleDistribution(request);
        validateGateConfiguration(site, request);

        for (int floorNumber = 1; floorNumber <= request.getNumberOfFloors(); floorNumber++) {
            Floor floor = new Floor(site.getSiteId() + "-F" + floorNumber, floorNumber, request.getFloorWidth(), request.getFloorHeight());
            site.addFloor(floor);
            List<Integer> zoneCapacities = distributeZoneCapacities(request.getSlotsPerFloor(), request.getZonesPerFloor());
            List<VehicleType> sequence = buildVehicleTypeSequence(request.getSlotsPerFloor(), request.getVehicleDistribution().toVehicleTypeMap());
            List<ParkingZone> zones = createZones(floor, zoneCapacities, sequence);
            for (ParkingZone zone : zones) {
                floor.addZone(zone);
            }
        }
    }

    private void validateVehicleDistribution(CreateSiteRequest request) {
        Map<VehicleType, Integer> distribution = request.getVehicleDistribution().toVehicleTypeMap();
        if (distribution == null || distribution.isEmpty()) {
            throw new InvalidVehicleTypeDistributionException("Vehicle distribution cannot be empty");
        }
        int total = distribution.values().stream().mapToInt(Integer::intValue).sum();
        if (total != 100) {
            throw new InvalidVehicleTypeDistributionException("Vehicle distribution must sum to 100");
        }
        if (distribution.keySet().stream().anyMatch(v -> v == null)) {
            throw new InvalidVehicleTypeDistributionException("Vehicle distribution contains an invalid vehicle type");
        }
        if (distribution.values().stream().anyMatch(v -> v < 0)) {
            throw new InvalidVehicleTypeDistributionException("Vehicle distribution percentages must not be negative");
        }
    }

    private void validateGateConfiguration(ParkingSite site, CreateSiteRequest request) {
        List<GateRequest> gates = request.getGates();
        if (gates == null || gates.isEmpty()) {
            throw new InvalidLayoutException("At least one gate is required");
        }
        for (GateRequest gateRequest : gates) {
            if (gateRequest.getGateId() == null || gateRequest.getGateId().isBlank()) {
                throw new InvalidLayoutException("Gate ID is required");
            }
            if (site.getGates().stream().anyMatch(g -> g.getGateId().equals(gateRequest.getGateId()))) {
                throw new InvalidLayoutException("Duplicate gate ID: " + gateRequest.getGateId());
            }
            if (gateRequest.getFloorNumber() < 1 || gateRequest.getFloorNumber() > request.getNumberOfFloors()) {
                throw new InvalidLayoutException("Gate floor is invalid: " + gateRequest.getFloorNumber());
            }
            double x = gateRequest.getCoordinate().getX();
            double y = gateRequest.getCoordinate().getY();
            boolean onBoundary = x == 0 || x == request.getFloorWidth() || y == 0 || y == request.getFloorHeight();
            if (!onBoundary) {
                throw new InvalidLayoutException("Gate must lie on the floor boundary: " + gateRequest.getGateId());
            }
            site.addGate(new Gate(gateRequest.getGateId(), gateRequest.getType(), gateRequest.getFloorNumber(), new Coordinate(x, y)));
        }
    }

    private List<Integer> distributeZoneCapacities(int totalSlots, int zonesPerFloor) {
        int base = totalSlots / zonesPerFloor;
        int remainder = totalSlots % zonesPerFloor;
        List<Integer> capacities = new ArrayList<>();
        for (int i = 0; i < zonesPerFloor; i++) {
            capacities.add(base + (i < remainder ? 1 : 0));
        }
        return capacities;
    }

    private List<ParkingZone> createZones(Floor floor, List<Integer> zoneCapacities, List<VehicleType> sequence) {
        List<ParkingZone> zones = new ArrayList<>();
        double gap = 8.0;
        int zoneCount = zoneCapacities.size();
        double zoneWidth = (floor.getWidth() - gap * (zoneCount - 1)) / zoneCount;
        double zoneHeight = floor.getHeight() - 10;
        int slotIndex = 0;
        for (int i = 0; i < zoneCount; i++) {
            double xStart = i * (zoneWidth + gap);
            double yStart = 5.0;
            ParkingZone zone = new ParkingZone(floor.getFloorId() + "-Z" + (i + 1), i + 1, new Coordinate(xStart, yStart), new Coordinate(xStart + zoneWidth, yStart + zoneHeight));
            zones.add(zone);
            int capacity = zoneCapacities.get(i);
            for (int slotNumber = 1; slotNumber <= capacity; slotNumber++) {
                int cols = Math.max(2, (int) Math.ceil(Math.sqrt(capacity)));
                int rows = (int) Math.ceil(capacity / (double) cols);
                int row = (slotNumber - 1) / cols;
                int col = (slotNumber - 1) % cols;
                double padding = 4.0;
                double slotWidth = Math.max(6.0, (zoneWidth - padding * 2) / cols);
                double slotHeight = Math.max(4.0, (zoneHeight - padding * 2) / rows);
                double x = xStart + padding + col * slotWidth;
                double y = yStart + padding + row * slotHeight;
                ParkingSlot slot = new ParkingSlot(
                        floor.getFloorId() + "-S" + slotNumber,
                        slotNumber,
                        floor.getFloorId(),
                        zone.getZoneId(),
                        sequence.get(slotIndex++),
                        new Coordinate(x, y));
                zone.addSlot(slot);
            }
        }
        return zones;
    }

    private List<VehicleType> buildVehicleTypeSequence(int totalSlots, Map<VehicleType, Integer> distribution) {
        List<VehicleType> sequence = new ArrayList<>();
        Map<VehicleType, Integer> counts = new LinkedHashMap<>();
        int remaining = totalSlots;
        int totalPercent = 0;
        for (VehicleType type : List.of(VehicleType.CAR, VehicleType.BIKE, VehicleType.TRUCK)) {
            int percent = distribution.getOrDefault(type, 0);
            int count = (int) Math.floor(totalSlots * percent / 100.0);
            counts.put(type, count);
            remaining -= count;
            totalPercent += percent;
        }
        int extra = remaining;
        for (VehicleType type : List.of(VehicleType.CAR, VehicleType.BIKE, VehicleType.TRUCK)) {
            while (extra > 0 && counts.get(type) < totalSlots) {
                counts.put(type, counts.get(type) + 1);
                extra--;
            }
        }
        for (VehicleType type : List.of(VehicleType.CAR, VehicleType.BIKE, VehicleType.TRUCK)) {
            for (int i = 0; i < counts.get(type); i++) {
                sequence.add(type);
            }
        }
        while (sequence.size() < totalSlots) {
            sequence.add(VehicleType.CAR);
        }
        return sequence.subList(0, totalSlots);
    }
}
