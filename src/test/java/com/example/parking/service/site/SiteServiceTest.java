package com.example.parking.service.site;

import com.example.parking.domain.enums.GateType;
import com.example.parking.domain.enums.SiteStatus;
import com.example.parking.domain.model.ParkingSite;
import com.example.parking.dto.request.CoordinateRequest;
import com.example.parking.dto.request.CreateSiteRequest;
import com.example.parking.dto.request.GateRequest;
import com.example.parking.dto.request.VehicleDistributionRequest;
import com.example.parking.exception.InvalidSiteStateException;
import com.example.parking.exception.SiteNotFoundException;
import com.example.parking.mapper.ObjectMapper;
import com.example.parking.repository.SiteRepository;
import com.example.parking.service.layout.LayoutGenerationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SiteServiceTest {

    private SiteRepository siteRepository;
    private LayoutGenerationService layoutGenerationService;
    private ObjectMapper mapper;
    private SiteService siteService;

    @BeforeEach
    void setUp() {
        siteRepository = Mockito.mock(SiteRepository.class);
        layoutGenerationService = Mockito.mock(LayoutGenerationService.class);
        mapper = new ObjectMapper();
        siteService = new SiteService(siteRepository, layoutGenerationService, mapper);
    }

    @Test
    void createSite_shouldGenerateLayoutAndSaveSite() {
        CreateSiteRequest request = new CreateSiteRequest();
        request.setName("Test Site");
        request.setNumberOfFloors(1);
        request.setSlotsPerFloor(2);
        request.setZonesPerFloor(1);
        request.setFloorWidth(20);
        request.setFloorHeight(20);
        request.setHourlyRate(30);
        request.setVehicleDistribution(new VehicleDistributionRequest(Map.of("CAR", 100)));
        request.setGates(List.of(new GateRequest("ENTRY-1", GateType.ENTRY, 1, new CoordinateRequest(0, 10))));

        when(siteRepository.save(any(ParkingSite.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ParkingSite site = siteService.createSite(request);

        assertThat(site.getName()).isEqualTo("Test Site");
        assertThat(site.getStatus()).isEqualTo(SiteStatus.DRAFT);
        verify(layoutGenerationService, times(1)).generateLayout(site, request);
        verify(siteRepository, times(1)).save(site);
    }

    @Test
    void getSite_shouldReturnSite_whenExists() {
        ParkingSite site = new ParkingSite("SITE-1", "Test Site", SiteStatus.DRAFT, Map.of(), 10, 1, 1, 10, 10);
        when(siteRepository.findById("SITE-1")).thenReturn(Optional.of(site));

        ParkingSite found = siteService.getSite("SITE-1");

        assertThat(found).isSameAs(site);
    }

    @Test
    void getSite_shouldThrowSiteNotFoundException_whenMissing() {
        when(siteRepository.findById("MISSING")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> siteService.getSite("MISSING"))
                .isInstanceOf(SiteNotFoundException.class)
                .hasMessageContaining("MISSING");
    }

    @Test
    void activateSite_shouldSetActive_whenDraft() {
        ParkingSite site = new ParkingSite("SITE-1", "Test Site", SiteStatus.DRAFT, Map.of(), 10, 1, 1, 10, 10);
        when(siteRepository.findById("SITE-1")).thenReturn(Optional.of(site));
        when(siteRepository.save(any(ParkingSite.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ParkingSite active = siteService.activateSite("SITE-1");

        assertThat(active.getStatus()).isEqualTo(SiteStatus.ACTIVE);
        verify(siteRepository).save(site);
    }

    @Test
    void activateSite_shouldThrowInvalidSiteStateException_whenNotDraft() {
        ParkingSite site = new ParkingSite("SITE-1", "Test Site", SiteStatus.ACTIVE, Map.of(), 10, 1, 1, 10, 10);
        when(siteRepository.findById("SITE-1")).thenReturn(Optional.of(site));

        assertThatThrownBy(() -> siteService.activateSite("SITE-1"))
                .isInstanceOf(InvalidSiteStateException.class)
                .hasMessageContaining("Only DRAFT sites can be activated");
    }

    @Test
    void closeSite_shouldSetClosed_whenActive() {
        ParkingSite site = new ParkingSite("SITE-1", "Test Site", SiteStatus.ACTIVE, Map.of(), 10, 1, 1, 10, 10);
        when(siteRepository.findById("SITE-1")).thenReturn(Optional.of(site));
        when(siteRepository.save(any(ParkingSite.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ParkingSite closed = siteService.closeSite("SITE-1");

        assertThat(closed.getStatus()).isEqualTo(SiteStatus.CLOSED);
        verify(siteRepository).save(site);
    }

    @Test
    void closeSite_shouldThrowInvalidSiteStateException_whenNotActive() {
        ParkingSite site = new ParkingSite("SITE-1", "Test Site", SiteStatus.DRAFT, Map.of(), 10, 1, 1, 10, 10);
        when(siteRepository.findById("SITE-1")).thenReturn(Optional.of(site));

        assertThatThrownBy(() -> siteService.closeSite("SITE-1"))
                .isInstanceOf(InvalidSiteStateException.class)
                .hasMessageContaining("Only ACTIVE sites can be closed");
    }
}
