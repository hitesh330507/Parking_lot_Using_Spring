package com.example.parking.dto.request;

import com.example.parking.domain.enums.VehicleType;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.validation.constraints.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public class VehicleDistributionRequest {
    @NotNull(message = "vehicleDistribution is required")
    private Map<String, Integer> distribution = new LinkedHashMap<>();

    public VehicleDistributionRequest() {}

    public VehicleDistributionRequest(Map<?, Integer> distribution) {
        this.distribution = new LinkedHashMap<>();
        if (distribution != null) {
            distribution.forEach((key, value) -> this.distribution.put(String.valueOf(key), value));
        }
    }

    @JsonAnySetter
    public void addDistribution(String key, Integer value) {
        this.distribution.put(key, value);
    }

    @JsonSetter
    public void setDistribution(Map<String, Integer> distribution) {
        this.distribution = distribution == null ? new LinkedHashMap<>() : new LinkedHashMap<>(distribution);
    }

    public Map<String, Integer> getDistribution() {
        return distribution;
    }

    public Map<VehicleType, Integer> toVehicleTypeMap() {
        Map<VehicleType, Integer> result = new LinkedHashMap<>();
        if (distribution == null) {
            return result;
        }
        for (Map.Entry<String, Integer> entry : distribution.entrySet()) {
            result.put(VehicleType.valueOf(entry.getKey().toUpperCase()), entry.getValue());
        }
        return result;
    }
}
