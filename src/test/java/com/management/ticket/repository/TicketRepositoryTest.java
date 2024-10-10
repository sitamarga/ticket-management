package com.management.ticket.repository;

import com.management.ticket.entity.Ticket;
import com.management.ticket.entity.User;
import com.management.ticket.enums.TicketStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @création 10/10/2024
 * @projet ticket-management
 * @auteur tsyta.diallo
 * @package com.management.ticket.repository
 */
@DataJpaTest
class TicketRepositoryTest {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        ticketRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldReturnUserTikets(){
        var user = userRepository.save(User
                .builder()
                .username("demo")
                .email("demo@mail.com")
                .build());

        var ticket1 = Ticket
                .builder()
                .title("Ticket 1")
                .description("Desc ticket 1")
                .status(TicketStatus.IN_PROGRESS)
                .createBy(user)
                .build();

        var ticket2 = Ticket
                .builder()
                .title("Ticket 2")
                .description("Desc ticket 2")
                .status(TicketStatus.CANCELED)
                .assignedTo(user)
                .build();
        ticketRepository.saveAll(List.of(ticket1, ticket2));
        assertThat(ticketRepository.findUserTickets(user.getId())).contains(ticket1, ticket2);
    }
}