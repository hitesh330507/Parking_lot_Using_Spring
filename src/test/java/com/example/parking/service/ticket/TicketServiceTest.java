package com.example.parking.service.ticket;

import com.example.parking.domain.enums.TicketStatus;
import com.example.parking.domain.enums.VehicleType;
import com.example.parking.domain.model.ParkingTicket;
import com.example.parking.exception.TicketAlreadyClosedException;
import com.example.parking.exception.TicketNotFoundException;
import com.example.parking.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TicketServiceTest {

    private TicketRepository ticketRepository;
    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        ticketRepository = Mockito.mock(TicketRepository.class);
        ticketService = new TicketService(ticketRepository);
    }

    @Test
    void createTicket_shouldSaveAndReturnTicket() {
        when(ticketRepository.save(any(ParkingTicket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ParkingTicket ticket = ticketService.createTicket("KA01AB1234", VehicleType.CAR,
                "SITE-1", "F1", "Z1", "S1", "ENTRY-1", LocalDateTime.of(2026, 8, 10, 10, 0));

        assertThat(ticket.getTicketId()).startsWith("TKT-");
        assertThat(ticket.getVehicleNumber()).isEqualTo("KA01AB1234");
        assertThat(ticket.getVehicleType()).isEqualTo(VehicleType.CAR);
        assertThat(ticket.getStatus()).isEqualTo(TicketStatus.ACTIVE);

        ArgumentCaptor<ParkingTicket> captor = ArgumentCaptor.forClass(ParkingTicket.class);
        verify(ticketRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getSiteId()).isEqualTo("SITE-1");
    }

    @Test
    void getTicket_shouldReturnExistingTicket_whenFound() {
        ParkingTicket stored = new ParkingTicket("TKT-1234", "KA01AB1234", VehicleType.BIKE,
                "SITE-1", "F1", "Z1", "S1", "ENTRY-1", LocalDateTime.now());
        when(ticketRepository.findById("TKT-1234")).thenReturn(Optional.of(stored));

        ParkingTicket found = ticketService.getTicket("TKT-1234");

        assertThat(found).isSameAs(stored);
    }

    @Test
    void getTicket_shouldThrowTicketNotFoundException_whenMissing() {
        when(ticketRepository.findById("MISSING")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.getTicket("MISSING"))
                .isInstanceOf(TicketNotFoundException.class)
                .hasMessageContaining("MISSING");
    }

    @Test
    void closeTicket_shouldUpdateStatusAndSave() {
        ParkingTicket stored = new ParkingTicket("TKT-1234", "KA01AB1234", VehicleType.TRUCK,
                "SITE-1", "F1", "Z1", "S1", "ENTRY-1", LocalDateTime.of(2026, 8, 10, 10, 0));
        when(ticketRepository.findById("TKT-1234")).thenReturn(Optional.of(stored));
        when(ticketRepository.save(any(ParkingTicket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ParkingTicket closed = ticketService.closeTicket("TKT-1234", "EXIT-1", LocalDateTime.of(2026, 8, 10, 12, 0), 100);

        assertThat(closed.getStatus()).isEqualTo(TicketStatus.CLOSED);
        assertThat(closed.getExitGateId()).isEqualTo("EXIT-1");
        assertThat(closed.getFee()).isEqualTo(100);
        verify(ticketRepository, times(1)).save(stored);
    }

    @Test
    void closeTicket_shouldThrowTicketAlreadyClosedException_whenAlreadyClosed() {
        ParkingTicket stored = new ParkingTicket("TKT-1234", "KA01AB1234", VehicleType.CAR,
                "SITE-1", "F1", "Z1", "S1", "ENTRY-1", LocalDateTime.of(2026, 8, 10, 10, 0));
        stored.close("EXIT-1", LocalDateTime.of(2026, 8, 10, 11, 0), 50);
        when(ticketRepository.findById("TKT-1234")).thenReturn(Optional.of(stored));

        assertThatThrownBy(() -> ticketService.closeTicket("TKT-1234", "EXIT-2", LocalDateTime.of(2026, 8, 10, 12, 0), 80))
                .isInstanceOf(TicketAlreadyClosedException.class)
                .hasMessageContaining("TKT-1234");
    }
}
