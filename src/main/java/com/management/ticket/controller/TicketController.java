package com.management.ticket.controller;

import com.management.ticket.dto.CustomResponse;
import com.management.ticket.dto.TicketDto;
import com.management.ticket.entity.Ticket;
import com.management.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @création 10/10/2024
 * @projet ticket-management
 * @auteur tsyta.diallo
 * @package com.management.ticket.controller
 */
@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/")
    public CustomResponse<?> getAllTickets(){
        return CustomResponse.<List<Ticket>>builder()
                .code(200)
                .content(ticketService.findAllTickets())
                .message("Succès!")
                .build();
    }
    @PostMapping("/{id}")
    public CustomResponse<?> getTicket(@PathVariable("id") Long id){
        return CustomResponse.<Ticket>builder()
                .code(200)
                .message("Succès!")
                .content(ticketService.findById(id))
                .build();
    }

    @PostMapping("/")
    public CustomResponse<?> add(@RequestBody TicketDto dto){
        return CustomResponse.<Ticket>builder()
                .code(200)
                .message("Succès!")
                .content(ticketService.add(dto))
                .build();
    }
    @PutMapping("/{id}")
    public CustomResponse<?> update(@PathVariable("id") Long id, @RequestBody TicketDto dto){
        ticketService.update(id, dto);
        return CustomResponse.builder()
                .code(200)
                .message("Succès!")
                .build();
    }
    @DeleteMapping("/{id}")
    public CustomResponse<?> deleteTicket(@PathVariable("id") Long id){
        ticketService.deleteTiket(id);
        return CustomResponse.builder()
                .code(200)
                .message("Succès!")
                .build();
    }
    @PutMapping("/{id}/assign/{userId}")
    public CustomResponse<?> assignTicket(@PathVariable("id") Long id, @PathVariable("userId") Long userId){
        ticketService.assignTo(id, userId);
        return CustomResponse.builder()
                .code(200)
                .message("Succès!")
                .build();
    }

}
