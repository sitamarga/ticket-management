package com.management.ticket.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @création 10/10/2024
 * @projet ticket-management
 * @auteur tsyta.diallo
 * @package com.management.ticket.enums
 */
@RequiredArgsConstructor
@Getter
public enum TicketStatus {
    IN_PROGRESS("en cours"),
    CANCELED("annulé"),
    FINISHED("terminé");

    private final String value;
}
