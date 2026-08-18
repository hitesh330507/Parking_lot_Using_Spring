package com.example.parking.service.site;

import com.example.parking.domain.enums.SiteStatus;
import com.example.parking.domain.model.ParkingSite;
import com.example.parking.dto.request.CreateSiteRequest;
import com.example.parking.dto.request.GateRequest;
import com.example.parking.exception.InvalidLayoutException;
import com.example.parking.exception.InvalidSiteStateException;
import com.example.parking.exception.SiteNotFoundException;
import com.example.parking.mapper.ObjectMapper;
import com.example.parking.repository.SiteRepository;
import com.example.parking.service.layout.LayoutGenerationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SiteService {
    private final SiteRepository siteRepository;
    private final LayoutGenerationService layoutGenerationService;
    private final ObjectMapper mapper;

    public SiteService(SiteRepository siteRepository, LayoutGenerationService layoutGenerationService, ObjectMapper mapper) {
        this.siteRepository = siteRepository;
        this.layoutGenerationService = layoutGenerationService;
        this.mapper = mapper;
    }

    public ParkingSite createSite(CreateSiteRequest request) {
        ParkingSite site = mapper.toDomain(request, "SITE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        layoutGenerationService.generateLayout(site, request);
        siteRepository.save(site);
        return site;
    }

    public ParkingSite getSite(String siteId) {
        return siteRepository.findById(siteId).orElseThrow(() -> new SiteNotFoundException(siteId));
    }

    public List<ParkingSite> getAllSites() {
        return siteRepository.findAll();
    }

    public ParkingSite activateSite(String siteId) {
        ParkingSite site = getSite(siteId);
        if (site.getStatus() != SiteStatus.DRAFT) {
            throw new InvalidSiteStateException("Only DRAFT sites can be activated");
        }
        site.setStatus(SiteStatus.ACTIVE);
        return siteRepository.save(site);
    }

    public ParkingSite closeSite(String siteId) {
        ParkingSite site = getSite(siteId);
        if (site.getStatus() != SiteStatus.ACTIVE) {
            throw new InvalidSiteStateException("Only ACTIVE sites can be closed");
        }
        site.setStatus(SiteStatus.CLOSED);
        return siteRepository.save(site);
    }
}
