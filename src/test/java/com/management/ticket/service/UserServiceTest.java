package com.management.ticket.service;

import com.management.ticket.dto.UserInfoDto;
import com.management.ticket.entity.User;
import com.management.ticket.exception.UserException;
import com.management.ticket.repository.TicketRepository;
import com.management.ticket.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @création 10/10/2024
 * @projet ticket-management
 * @auteur tsyta.diallo
 * @package com.management.ticket.service
 */

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private TicketRepository ticketRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, ticketRepository);
    }

    @Test
    void shouldGetAllUsers() {
        userService.findAll();
        verify(userRepository).findAll();
    }

    @Test
    void shouldAddNewUser() {
        UserInfoDto userInfo = new UserInfoDto(
                "demo",
                "demo@mail.com"
        );
        userService.add(userInfo);

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository)
                .save(argumentCaptor.capture());
        User capturedUser = argumentCaptor.getValue();
        assertEquals(capturedUser.getUsername(), userInfo.getUsername());
        assertEquals(capturedUser.getEmail(), userInfo.getEmail());
    }

    @Test
    void shouldThrownExceptionWhenUsernameIsEmptyOrNull() {
        UserInfoDto userInfo = new UserInfoDto(
                null,
                "demo@mail.com"
        );
        assertThatThrownBy(() -> userService.add(userInfo))
                .isInstanceOf(UserException.class)
                .hasMessageContaining("Utilisateur introuvable.");
    }

    @Test
    void shouldThrownExceptionWhenEmailIsEmptyOrNull() {
        UserInfoDto userInfo = new UserInfoDto(
                "demo",
                null
        );
        assertThatThrownBy(() -> userService.add(userInfo))
                .isInstanceOf(UserException.class)
                .hasMessageContaining("Vous devez renseigner l'adresse email");
    }

    @Test
    void shouldThrownExceptionWhenUsernameIsTaken() {
        UserInfoDto userInfo = new UserInfoDto(
                "demo",
                "demo@mail.com"
        );
        given(userRepository.existsByUsername(anyString())).willReturn(true);
        assertThatThrownBy(() -> userService.add(userInfo))
                .isInstanceOf(UserException.class)
                .hasMessageContaining(String.format("Le nom d'utilisateur %s est déjà utilisé.", userInfo.getUsername()));
    }

    @Test
    void shouldThrownExceptionWhenEmailIsTaken() {
        String email = "demo@mail.com";
        UserInfoDto userInfo = new UserInfoDto(
                "demo",
                email
        );
        given(userRepository.existsByEmail(anyString())).willReturn(true);
        assertThatThrownBy(() -> userService.add(userInfo))
                .isInstanceOf(UserException.class)
                .hasMessageContaining(String.format("L'adresse email %s est déjà utilisée.", userInfo.getEmail()));
    }

    @Test
    void shouldGetUserTickes() {
        given(userRepository.existsById(anyLong())).willReturn(true);
        userService.findUserTickets(anyLong());
        verify(ticketRepository).findUserTickets(anyLong());
    }

    @Test
    void shouldThrownAnExceptionWhenGettingUserTickesWhileUserIdDoesNotExists() {
        given(userRepository.existsById(anyLong())).willReturn(false);
        assertThatThrownBy(() -> userService.findUserTickets(anyLong()))
                .isInstanceOf(UserException.class)
                .hasMessageContaining("L'utilisateur avec l'id spécifié n'existe pas.");

    }

    @Test
    void shouldUpdateUserEmail() {
        long id = 1L;
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(User.builder()
                .email("demo@mail.com")
                .username("demo")
                .id(id)
                .build()));
        UserInfoDto updateInfo = new UserInfoDto(
                "demo",
                "demo2@mail.com"
        );

        userService.update(id, updateInfo);

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository)
                .save(argumentCaptor.capture());
        User capturedUser = argumentCaptor.getValue();
        assertEquals(capturedUser.getEmail(), updateInfo.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNullOnUpdate() {
        UserInfoDto userInfo = new UserInfoDto();
        userInfo.setEmail(null);

        assertThatThrownBy(() -> userService.update(1L, userInfo))
                .isInstanceOf(UserException.class)
                .hasMessageContaining("Vous devez renseigner l'adresse email");
    }

    @Test
    void shouldThrowExceptionWhenEmailIsEmptyOnUpdate() {
        UserInfoDto userInfo = new UserInfoDto();
        userInfo.setEmail(" ");

        assertThatThrownBy(() -> userService.update(1L, userInfo))
                .isInstanceOf(UserException.class)
                .hasMessageContaining("Vous devez renseigner l'adresse email");
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExistOnUpdate() {
        UserInfoDto userInfo = new UserInfoDto();
        userInfo.setEmail("demo@mail.com");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.update(1L, userInfo))
                .isInstanceOf(UserException.class)
                .hasMessageContaining("Le nom d'utilisateur spécifié n'existe pas.");
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExistsOnUpdate() {
        UserInfoDto userInfo = new UserInfoDto();
        userInfo.setEmail("demo@mail.com");

        var user = User.builder().id(1L).username("demo").email("old@mail.com").build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(userInfo.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> userService.update(1L, userInfo))
                .isInstanceOf(UserException.class)
                .hasMessageContaining("Cette adresse email est déjà utilisée.");
    }
}