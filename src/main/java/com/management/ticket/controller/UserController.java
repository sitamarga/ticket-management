package com.management.ticket.controller;

import com.management.ticket.dto.CustomResponse;
import com.management.ticket.dto.UserInfoDto;
import com.management.ticket.entity.Ticket;
import com.management.ticket.entity.User;
import com.management.ticket.service.UserService;
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
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/")
    public CustomResponse<?> getAllUser(){
        return CustomResponse.<List<User>>builder()
                .code(200)
                .content(userService.findAll())
                .message("Succès!")
                .build();
    }
    @PostMapping("/add")
    public CustomResponse<?> add(@RequestBody UserInfoDto dto){
        return CustomResponse.<User>builder()
                .code(200)
                .message("Succès!")
                .content(userService.add(dto))
                .build();
    }
    @PutMapping("/{id}")
    public CustomResponse<?> update(@PathVariable("id") Long id, @RequestBody UserInfoDto dto){
        userService.update(id, dto);
        return CustomResponse.<List<Ticket>>builder()
                .code(200)
                .message("Succès!")
                .build();
    }
    @GetMapping("/{id}/ticket")
    public CustomResponse<?> getUserTickets(@PathVariable("id") Long id){
        return CustomResponse.<List<Ticket>>builder()
                .code(200)
                .message("Succès!")
                .content(userService.findUserTickets(id))
                .build();
    }
}
