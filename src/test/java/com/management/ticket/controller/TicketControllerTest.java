package com.management.ticket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.management.ticket.dto.TicketDto;
import com.management.ticket.entity.Ticket;
import com.management.ticket.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TicketControllerTest {

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private TicketController ticketController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ticketController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldReturnAllTickets() throws Exception {
        var ticket = new Ticket();
        var tickets = List.of(ticket);

        when(ticketService.findAllTickets()).thenReturn(tickets);

        mockMvc.perform(get("/tickets/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Succès!"))
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void shouldReturnTicketById() throws Exception {
        Long ticketId = 1L;
        var ticket = new Ticket();
        when(ticketService.findById(ticketId)).thenReturn(ticket);
        mockMvc.perform(post("/tickets/{id}", ticketId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Succès!"));
    }

    @Test
    void shouldReturnCreatedTicket() throws Exception {
        var dto = new TicketDto();
        var ticket = new Ticket();
        when(ticketService.add(dto)).thenReturn(ticket);
        mockMvc.perform(post("/tickets/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Succès!"));
    }

    @Test
    void shouldUpdateTicket() throws Exception {
        var dto = new TicketDto();
        Long ticketId = 1L;
        mockMvc.perform(put("/tickets/{id}", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Succès!"));
        verify(ticketService).update(ticketId, dto);
    }

    @Test
    void shouldDeleteTicket() throws Exception {
        Long ticketId = 1L;
        mockMvc.perform(delete("/tickets/{id}", ticketId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Succès!"));
        verify(ticketService).deleteTiket(ticketId);
    }

    @Test
    void shouldAssignTicket() throws Exception {
        Long ticketId = 1L;
        Long userId = 2L;
        mockMvc.perform(put("/tickets/{id}/assign/{userId}", ticketId, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Succès!"));
        verify(ticketService).assignTo(ticketId, userId);
    }
}