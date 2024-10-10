package com.management.ticket.service;

import com.management.ticket.dto.TicketDto;
import com.management.ticket.dto.UserInfoDto;
import com.management.ticket.entity.Ticket;
import com.management.ticket.entity.User;
import com.management.ticket.enums.TicketStatus;
import com.management.ticket.exception.TicketException;
import com.management.ticket.exception.UserException;
import com.management.ticket.repository.TicketRepository;
import com.management.ticket.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @création 10/10/2024
 * @projet ticket-management
 * @auteur tsyta.diallo
 * @package com.management.ticket.service
 */
@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private UserRepository userRepository;
    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        ticketService = new TicketService(ticketRepository, userRepository);
    }

    @Test
    void shouldGetAllTickets() {
        ticketService.findAllTickets();
        verify(ticketRepository).findAll();
    }

    @Test
    void shouldDeleteTicketById() {
        ticketService.deleteTiket(anyLong());
        verify(ticketRepository).deleteById(anyLong());
    }

    @Test
    void shouldFindTicketById() {
        var ticket = Ticket.builder().id(1L).build();
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        var result = ticketService.findById(1L);
        assertNotNull(result);
        assertEquals(ticket.getId(), result.getId());
        verify(ticketRepository).findById(1L);
    }

    @Test
    void shouldThrowTicketExceptionTicketDoesNotExist() {
        when(ticketRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.findById(1L))
                .isInstanceOf(TicketException.class)
                .hasMessageContaining("Ce ticket n'a pas été trouvé.");
        verify(ticketRepository).findById(1L);
    }

    @Test
    void shouldAssignTicketToUser() {
        var ticket = Ticket.builder().id(1L).build();
        when(ticketRepository.findById(anyLong())).thenReturn(Optional.of(ticket));
        when(userRepository.existsById(2L)).thenReturn(true);

        ticketService.assignTo(1L, 2L);

        assertEquals(2L, ticket.getAssignedTo().getId());
        verify(ticketRepository).save(ticket);
    }

    @Test
    void shouldThrowTicketExceptionWhenTicketDoesNotExistWhileAssigning() {
        when(ticketRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> ticketService.assignTo(anyLong(), 2L))
                .isInstanceOf(TicketException.class)
                .hasMessageContaining("Ticket introuvable.");
    }

    @Test
    void shouldThrowTicketExceptionWhenUserDoesNotExistWhileAssigning() {
        when(ticketRepository.findById(anyLong())).thenReturn(Optional.of(Ticket.builder().id(1L).build()));
        when(userRepository.existsById(anyLong())).thenReturn(false);
        assertThatThrownBy(() -> ticketService.assignTo(anyLong(), 2L))
                .isInstanceOf(TicketException.class)
                .hasMessageContaining("Utilisateur introuvable.");
    }

    @Test
    void shouldAddNewTicket() {
        var dto = TicketDto
                .builder()
                .title("Ticket 1")
                .description(" Descc ticket 1")
                .userId(1L)
                .build();
        given(userRepository.findById(1L)).willReturn(Optional.of(User.builder().id(1L).build()));
        ticketService.add(dto);
        ArgumentCaptor<Ticket> argumentCaptor = ArgumentCaptor.forClass(Ticket.class);
        verify(ticketRepository)
                .save(argumentCaptor.capture());
        var capturedTicket = argumentCaptor.getValue();
        assertEquals(capturedTicket.getTitle(), dto.getTitle());
        assertEquals(capturedTicket.getStatus(), TicketStatus.IN_PROGRESS);
        assertEquals(capturedTicket.getCreateBy().getId(), dto.getUserId());
        assertEquals(capturedTicket.getDescription(), dto.getDescription());
    }

    @Test
    void shouldThrowTicketExceptionWhenUserCreationUserDoesNotExists() {
        var dto = TicketDto
                .builder()
                .title("Ticket 1")
                .description(" Descc ticket 1")
                .userId(1L)
                .build();
        given(userRepository.findById(1L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> ticketService.add(dto))
                .isInstanceOf(TicketException.class)
                .hasMessageContaining("Utilisateur introuvable.");
    }

    @Test
    void shouldUpdateTicket() {
        when(ticketRepository.findById(anyLong())).thenReturn(Optional.of(Ticket
                .builder()
                .title("Ticket 1")
                .description("Desc ticket 1")
                .status(TicketStatus.IN_PROGRESS)
                .id(1L)
                .build()));

        var dto = TicketDto
                .builder()
                .title("Ticket 1.1")
                .description("Desc ticket 1.1")
                .build();

        ticketService.update(1L, dto);

        ArgumentCaptor<Ticket> argumentCaptor = ArgumentCaptor.forClass(Ticket.class);
        verify(ticketRepository)
                .save(argumentCaptor.capture());
        var capturedTicket = argumentCaptor.getValue();
        assertEquals(capturedTicket.getTitle(), dto.getTitle());
        assertEquals(capturedTicket.getStatus(), TicketStatus.IN_PROGRESS);
        assertEquals(capturedTicket.getDescription(), dto.getDescription());
    }

    @Test
    void shouldThrowTicketExceptionWhenTicketDoesNotExists() {
        given(ticketRepository.findById(anyLong())).willReturn(Optional.empty());
        assertThatThrownBy(() -> ticketService.update(1L, any()))
                .isInstanceOf(TicketException.class)
                .hasMessageContaining("Ticket introuvable.");
    }

}