package com.example.parking;

import com.example.parking.dto.request.CreateSiteRequest;
import com.example.parking.dto.request.GateRequest;
import com.example.parking.dto.request.ParkingEntryRequest;
import com.example.parking.dto.request.ParkingExitRequest;
import com.example.parking.dto.request.VehicleDistributionRequest;
import com.example.parking.dto.request.CoordinateRequest;
import com.example.parking.dto.response.CreateSiteResponse;
import com.example.parking.dto.response.ParkingEntryResponse;
import com.example.parking.dto.response.ParkingExitResponse;
import com.example.parking.dto.response.TicketResponse;
import com.example.parking.domain.enums.GateType;
import com.example.parking.domain.enums.SiteStatus;
import com.example.parking.domain.enums.VehicleType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ParkingApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void fullParkingLifecycleWorks() throws Exception {
        CreateSiteRequest request = new CreateSiteRequest();
        request.setName("City Mall Parking");
        request.setNumberOfFloors(1);
        request.setSlotsPerFloor(4);
        request.setZonesPerFloor(2);
        request.setFloorWidth(100);
        request.setFloorHeight(50);
        request.setHourlyRate(50);
        request.setVehicleDistribution(new VehicleDistributionRequest(Map.of(VehicleType.CAR, 50, VehicleType.BIKE, 25, VehicleType.TRUCK, 25)));
        request.setGates(List.of(
                new GateRequest("ENTRY-1", GateType.ENTRY, 1, new CoordinateRequest(0, 25)),
                new GateRequest("EXIT-1", GateType.EXIT, 1, new CoordinateRequest(100, 25))
        ));

        MvcResult createResult = mockMvc.perform(post("/sites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "name":"City Mall Parking",
                          "numberOfFloors":1,
                          "slotsPerFloor":4,
                          "zonesPerFloor":2,
                          "floorWidth":100,
                          "floorHeight":50,
                          "hourlyRate":50,
                          "vehicleDistribution":{"CAR":50,"BIKE":25,"TRUCK":25},
                          "gates":[
                            {"gateId":"ENTRY-1","type":"ENTRY","floorNumber":1,"coordinate":{"x":0,"y":25}},
                            {"gateId":"EXIT-1","type":"EXIT","floorNumber":1,"coordinate":{"x":100,"y":25}}
                          ]
                        }
                        """))
                .andExpect(status().isCreated())
                .andReturn();

        CreateSiteResponse created = new com.example.parking.mapper.ObjectMapper().readValue(createResult.getResponse().getContentAsString(), CreateSiteResponse.class);
        assertThat(created.getStatus()).isEqualTo(SiteStatus.DRAFT.name());

        mockMvc.perform(post("/sites/" + created.getSiteId() + "/activate"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/sites/" + created.getSiteId()))
                .andExpect(status().isOk());

        ParkingEntryRequest entryRequest = new ParkingEntryRequest();
        entryRequest.setSiteId(created.getSiteId());
        entryRequest.setEntryGateId("ENTRY-1");
        entryRequest.setVehicleNumber("KA01AB1234");
        entryRequest.setVehicleType(VehicleType.CAR);
        entryRequest.setEntryTime(LocalDateTime.of(2026, 8, 9, 10, 0));

        MvcResult entryResult = mockMvc.perform(post("/parking/entry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "siteId":"%s",
                          "entryGateId":"ENTRY-1",
                          "vehicleNumber":"KA01AB1234",
                          "vehicleType":"CAR",
                          "entryTime":"2026-08-09T10:00:00"
                        }
                        """.formatted(created.getSiteId())))
                .andExpect(status().isCreated())
                .andReturn();

        ParkingEntryResponse entry = new com.example.parking.mapper.ObjectMapper().readValue(entryResult.getResponse().getContentAsString(), ParkingEntryResponse.class);
        assertThat(entry.getTicketId()).isNotBlank();

        MvcResult ticketResult = mockMvc.perform(get("/parking/tickets/" + entry.getTicketId()))
                .andExpect(status().isOk())
                .andReturn();
        TicketResponse ticket = new com.example.parking.mapper.ObjectMapper().readValue(ticketResult.getResponse().getContentAsString(), TicketResponse.class);
        assertThat(ticket.getStatus()).isEqualTo("ACTIVE");

        mockMvc.perform(get("/sites/" + created.getSiteId() + "/availability"))
                .andExpect(status().isOk());

        ParkingExitRequest exitRequest = new ParkingExitRequest();
        exitRequest.setTicketId(entry.getTicketId());
        exitRequest.setExitGateId("EXIT-1");
        exitRequest.setExitTime(LocalDateTime.of(2026, 8, 9, 12, 30));

        MvcResult exitResult = mockMvc.perform(post("/parking/exit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "ticketId":"%s",
                          "exitGateId":"EXIT-1",
                          "exitTime":"2026-08-09T12:30:00"
                        }
                        """.formatted(entry.getTicketId())))
                .andExpect(status().isOk())
                .andReturn();

        ParkingExitResponse exit = new com.example.parking.mapper.ObjectMapper().readValue(exitResult.getResponse().getContentAsString(), ParkingExitResponse.class);
        assertThat(exit.getFee()).isGreaterThan(0);

        MvcResult closedTicketResult = mockMvc.perform(get("/parking/tickets/" + entry.getTicketId()))
                .andExpect(status().isOk())
                .andReturn();
        TicketResponse closedTicket = new com.example.parking.mapper.ObjectMapper().readValue(closedTicketResult.getResponse().getContentAsString(), TicketResponse.class);
        assertThat(closedTicket.getStatus()).isEqualTo("CLOSED");
    }
}
