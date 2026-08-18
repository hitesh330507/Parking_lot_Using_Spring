package com.example.parking.service.parking;

import com.example.parking.domain.enums.SlotStatus;
import com.example.parking.domain.enums.VehicleType;
import com.example.parking.domain.model.ParkingSlot;
import com.example.parking.domain.model.ParkingSite;
import com.example.parking.domain.model.ParkingTicket;
import com.example.parking.exception.NoAvailableSlotException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Service
public class ParkingAllocationService {
    private final ConcurrentMap<String, ParkingTicket> activeVehicles = new ConcurrentHashMap<>();

    public synchronized ParkingTicket allocateSlot(ParkingSite site, String gateId, String vehicleNumber, VehicleType vehicleType) {
        if (activeVehicles.containsKey(vehicleNumber)) {
            throw new NoAvailableSlotException("Vehicle is already parked");
        }
        List<ParkingSlot> candidates = site.getFloors().stream()
                .flatMap(floor -> floor.getZones().stream())
                .flatMap(zone -> zone.getSlots().stream())
                .filter(slot -> slot.getVehicleType() == vehicleType)
                .filter(slot -> slot.getStatus() == SlotStatus.AVAILABLE)
                .sorted(Comparator.comparing(ParkingSlot::getSlotNumber))
                .toList();
        if (candidates.isEmpty()) {
            throw new NoAvailableSlotException("No compatible slot available for vehicle type " + vehicleType);
        }
        ParkingSlot chosen = candidates.get(0);
        chosen.occupy();
        activeVehicles.put(vehicleNumber, null);
        return null;
    }
}
