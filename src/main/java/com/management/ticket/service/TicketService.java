package com.management.ticket.service;

import com.management.ticket.dto.TicketDto;
import com.management.ticket.entity.Ticket;
import com.management.ticket.entity.User;
import com.management.ticket.enums.TicketStatus;
import com.management.ticket.exception.TicketException;
import com.management.ticket.repository.TicketRepository;
import com.management.ticket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @création 10/10/2024
 * @projet ticket-management
 * @auteur tsyta.diallo
 * @package com.management.ticket.service
 */
@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public List<Ticket> findAllTickets(){
        return ticketRepository.findAll();
    }

    public void deleteTiket(Long ticketId){
        ticketRepository.deleteById(ticketId);
    }

    public Ticket findById(Long ticketId) throws TicketException{
        return ticketRepository
                .findById(ticketId)
                .orElseThrow(()
                        -> new TicketException("Ce ticket n'a pas été trouvé."));
    }

    public void assignTo(Long ticketId, Long userId) {
        var ticket = ticketRepository
                .findById(ticketId)
                .orElseThrow(()
                        -> new TicketException("Ticket introuvable."));
        if (!userRepository.existsById(userId))
            throw new TicketException("Utilisateur introuvable.");
        ticket.setAssignedTo(User
                .builder()
                .id(userId)
                .build());
        ticketRepository.save(ticket);
    }

    public Ticket add(TicketDto dto) {
        var user = userRepository
                .findById(dto.getUserId())
                .orElseThrow(()
                        -> new TicketException("Utilisateur introuvable."));
        var ticket = Ticket
                .builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(TicketStatus.IN_PROGRESS)
                .createBy(user)
                .build();
        return ticketRepository.save(ticket);
    }

    public void update(Long ticketId, TicketDto dto) {
        var ticket = ticketRepository
                .findById(ticketId)
                .orElseThrow(()
                        -> new TicketException("Ticket introuvable."));

        ticket.setTitle(dto.getTitle());
        ticket.setDescription(dto.getDescription());
        ticketRepository.save(ticket);
    }
}
