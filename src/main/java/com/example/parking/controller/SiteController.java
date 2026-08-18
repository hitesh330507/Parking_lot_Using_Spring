package com.example.parking.controller;

import com.example.parking.domain.model.ParkingSite;
import com.example.parking.dto.request.CreateSiteRequest;
import com.example.parking.dto.response.AvailabilityResponse;
import com.example.parking.dto.response.CreateSiteResponse;
import com.example.parking.dto.response.SiteResponse;
import com.example.parking.mapper.ObjectMapper;
import com.example.parking.service.availability.AvailabilityService;
import com.example.parking.service.site.SiteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sites")
public class SiteController {
    private final SiteService siteService;
    private final ObjectMapper mapper;
    private final AvailabilityService availabilityService;

    public SiteController(SiteService siteService, ObjectMapper mapper, AvailabilityService availabilityService) {
        this.siteService = siteService;
        this.mapper = mapper;
        this.availabilityService = availabilityService;
    }

    @PostMapping
    public ResponseEntity<CreateSiteResponse> createSite(@Valid @RequestBody CreateSiteRequest request) {
        ParkingSite site = siteService.createSite(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toCreateSiteResponse(site));
    }

    @GetMapping
    public ResponseEntity<List<SiteResponse>> getAllSites() {
        return ResponseEntity.ok(siteService.getAllSites().stream().map(mapper::toSiteResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SiteResponse> getSite(@PathVariable String id) {
        return ResponseEntity.ok(mapper.toSiteResponse(siteService.getSite(id)));
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<CreateSiteResponse> activateSite(@PathVariable String id) {
        ParkingSite site = siteService.activateSite(id);
        return ResponseEntity.ok(new CreateSiteResponse(site.getSiteId(), site.getName(), site.getStatus().name()));
    }

    @PostMapping("/{id}/close")
    public ResponseEntity<CreateSiteResponse> closeSite(@PathVariable String id) {
        ParkingSite site = siteService.closeSite(id);
        return ResponseEntity.ok(new CreateSiteResponse(site.getSiteId(), site.getName(), site.getStatus().name()));
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<AvailabilityResponse> getAvailability(@PathVariable String id) {
        ParkingSite site = siteService.getSite(id);
        return ResponseEntity.ok(availabilityService.calculate(site));
    }
}
