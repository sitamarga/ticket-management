package com.management.ticket.dto;

import com.management.ticket.enums.TicketStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @création 10/10/2024
 * @projet ticket-management
 * @auteur tsyta.diallo
 * @package com.management.ticket.dto
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketDto {

    private String title;
    private String description;
    private Long userId;
}
