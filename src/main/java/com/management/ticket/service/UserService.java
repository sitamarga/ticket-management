package com.management.ticket.service;

import com.management.ticket.exception.UserException;
import com.management.ticket.dto.UserInfoDto;
import com.management.ticket.entity.Ticket;
import com.management.ticket.entity.User;
import com.management.ticket.repository.TicketRepository;
import com.management.ticket.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @création 10/10/2024
 * @projet ticket-management
 * @auteur tsyta.diallo
 * @package com.management.ticket.service
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User add(UserInfoDto dto) throws UserException {
        if (Objects.isNull(dto.getEmail()) || dto.getEmail().trim().isEmpty())
            throw new UserException("Vous devez renseigner l'adresse email");

        if (userRepository.existsByEmail(dto.getEmail()))
            throw new UserException(String.format("L'adresse email %s est déjà utilisée.", dto.getEmail()));

        if (Objects.isNull(dto.getUsername()) || dto.getUsername().trim().isEmpty())
            throw new UserException("Vous devez renseigner un nom d'utilisateur.");

        if (userRepository.existsByUsername(dto.getUsername()))
            throw new UserException(String.format("Le nom d'utilisateur %s est déjà utilisé.", dto.getUsername()));

        return userRepository.save(User
                .builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .build());
    }

    @Transactional
    public void update(Long userId, UserInfoDto dto) throws UserException {

        if (Objects.isNull(dto.getEmail()) || dto.getEmail().trim().isEmpty())
            throw new UserException("Vous devez renseigner l'adresse email");

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("Le nom d'utilisateur spécifié n'existe pas."));

        if (userRepository.existsByEmail(dto.getEmail()) && !user.getEmail().equals(dto.getEmail()))
            throw new UserException("Cette adresse email est déjà utilisée.");

        user.setEmail(dto.getEmail());
        userRepository.save(user);
    }

    public List<Ticket> findUserTickets(Long userId) throws UserException{
        if (!userRepository.existsById(userId))
            throw new UserException("L'utilisateur avec l'id spécifié n'existe pas.");
        return ticketRepository.findUserTickets(userId);
    }
}
