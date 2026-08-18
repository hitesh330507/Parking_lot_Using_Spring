package com.example.parking.service.availability;

import com.example.parking.domain.enums.SlotStatus;
import com.example.parking.domain.enums.VehicleType;
import com.example.parking.domain.model.Floor;
import com.example.parking.domain.model.ParkingSite;
import com.example.parking.domain.model.ParkingSlot;
import com.example.parking.domain.model.ParkingZone;
import com.example.parking.dto.response.AvailabilityResponse;
import com.example.parking.dto.response.FloorAvailabilityResponse;
import com.example.parking.dto.response.VehicleAvailabilityResponse;
import com.example.parking.mapper.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class AvailabilityService {
    private final ObjectMapper mapper;

    public AvailabilityService(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public AvailabilityResponse calculate(ParkingSite site) {
        int totalSlots = 0;
        int availableSlots = 0;
        int occupiedSlots = 0;
        Map<String, VehicleAvailabilityResponse> byVehicleType = new LinkedHashMap<>();
        Map<String, FloorAvailabilityResponse> byFloor = new LinkedHashMap<>();

        for (VehicleType type : VehicleType.values()) {
            VehicleAvailabilityResponse response = new VehicleAvailabilityResponse();
            response.setTotal(0);
            response.setAvailable(0);
            response.setOccupied(0);
            byVehicleType.put(type.name(), response);
        }

        for (Floor floor : site.getFloors()) {
            FloorAvailabilityResponse floorResponse = new FloorAvailabilityResponse();
            floorResponse.setTotal(0);
            floorResponse.setAvailable(0);
            floorResponse.setOccupied(0);
            byFloor.put(floor.getFloorId(), floorResponse);
            for (ParkingZone zone : floor.getZones()) {
                for (ParkingSlot slot : zone.getSlots()) {
                    totalSlots++;
                    floorResponse.setTotal(floorResponse.getTotal() + 1);
                    VehicleAvailabilityResponse vehicleResponse = byVehicleType.get(slot.getVehicleType().name());
                    vehicleResponse.setTotal(vehicleResponse.getTotal() + 1);
                    if (slot.getStatus() == SlotStatus.AVAILABLE) {
                        availableSlots++;
                        floorResponse.setAvailable(floorResponse.getAvailable() + 1);
                        vehicleResponse.setAvailable(vehicleResponse.getAvailable() + 1);
                    } else {
                        occupiedSlots++;
                        floorResponse.setOccupied(floorResponse.getOccupied() + 1);
                        vehicleResponse.setOccupied(vehicleResponse.getOccupied() + 1);
                    }
                }
            }
        }

        return mapper.toAvailabilityResponse(site, totalSlots, availableSlots, occupiedSlots, byVehicleType, byFloor);
    }
}
