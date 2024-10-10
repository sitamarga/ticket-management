package com.management.ticket.util;

import com.management.ticket.dto.CustomResponse;
import com.management.ticket.exception.TicketException;
import com.management.ticket.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @création 10/10/2024
 * @projet ticket-management
 * @auteur tsyta.diallo
 * @package com.management.ticket.controller
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomResponse<?> handleUserException(UserException e) {
        return CustomResponse.builder().message(e.getMessage()).code(500).build();
    }

    @ExceptionHandler(TicketException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomResponse<?> handleTicketException(TicketException e) {
        return CustomResponse.builder().message(e.getMessage()).code(500).build();
    }
}
