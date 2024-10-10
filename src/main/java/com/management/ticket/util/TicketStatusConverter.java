package com.management.ticket.util;

import com.management.ticket.enums.TicketStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @création 10/10/2024
 * @projet ticket-management
 * @auteur tsyta.diallo
 * @package com.management.ticket.converter
 */
@Converter(autoApply = true)
public class TicketStatusConverter implements AttributeConverter<TicketStatus, String> {
    @Override
    public String convertToDatabaseColumn(TicketStatus ticketStatus) {
        if (Objects.nonNull(ticketStatus))
            return ticketStatus.getValue();
        return null;
    }

    @Override
    public TicketStatus convertToEntityAttribute(String value) {
        return Stream
                .of(TicketStatus.values())
                .filter(ticketStatus -> ticketStatus.getValue().equals(value))
                .findFirst().orElse(null);
    }
}
