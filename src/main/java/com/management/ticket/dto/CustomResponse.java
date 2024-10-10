package com.management.ticket.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @création 10/10/2024
 * @projet ticket-management
 * @auteur tsyta.diallo
 * @package com.management.ticket.dto
 */
@Builder
@Data
public class CustomResponse<T> {

    private int code;
    private String message;
    private T content;
}
