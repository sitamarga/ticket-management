package com.management.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @création 10/10/2024
 * @projet ticket-management
 * @auteur tsyta.diallo
 * @package com.management.ticket.dto
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInfoDto {

    private String username;
    private String email;
}
