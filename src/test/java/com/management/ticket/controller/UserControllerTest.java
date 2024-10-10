package com.management.ticket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.management.ticket.dto.UserInfoDto;
import com.management.ticket.entity.Ticket;
import com.management.ticket.entity.User;
import com.management.ticket.service.UserService;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @création 10/10/2024
 * @projet ticket-management
 * @auteur tsyta.diallo
 * @package com.management.ticket.controller
 */
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        var user = new User();
        var users = Collections.singletonList(user);

        when(userService.findAll()).thenReturn(users);

        mockMvc.perform(get("/users/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Succès!"))
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void shouldReturnCreatedUser() throws Exception {
        var dto = new UserInfoDto();
        var user = new User();
        when(userService.add(dto)).thenReturn(user);

        mockMvc.perform(post("/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Succès!"));
    }

    @Test
    void shouldReturnSuccessMessageAfterUpdate() throws Exception {
        var dto = new UserInfoDto();

        mockMvc.perform(put("/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Succès!"));

        verify(userService).update(1L, dto);
    }

    @Test
    void shouldReturnUserTickets() throws Exception {
        var ticket = new Ticket();
        var tickets = Collections.singletonList(ticket);

        when(userService.findUserTickets(1L)).thenReturn(tickets);

        mockMvc.perform(get("/users/{id}/ticket", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Succès!"))
                .andExpect(jsonPath("$.content.length()").value(1));
    }
}