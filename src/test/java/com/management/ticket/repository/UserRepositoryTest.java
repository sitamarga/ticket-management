package com.management.ticket.repository;

import com.management.ticket.entity.User;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @création 10/10/2024
 * @projet ticket-management
 * @auteur tsyta.diallo
 * @package com.management.ticket.repository
 */
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void shouldCheckIfUsernameExists(){
        String username = "demo";
        User user = User
                .builder()
                .username(username)
                .email("demo@mail.com")
                .build();
        userRepository.save(user);
        boolean exists = userRepository.existsByUsername(username);
        assertTrue(exists);

    }

    @Test
    void shouldCheckIfUsernameDoesNotExists(){
        String username = "demo";
        boolean exists = userRepository.existsByUsername(username);
        assertFalse(exists);
    }

    @Test
    void shouldCheckIfEmailExists(){
        String email = "demo@mail.com";
        User user = User
                .builder()
                .username("demo")
                .email(email)
                .build();
        userRepository.save(user);
        boolean exists = userRepository.existsByEmail(email);
        assertTrue(exists);

    }

    @Test
    void shouldCheckIfEmailDoesNotExists(){
        String email = "demo@mail.com";
        boolean exists = userRepository.existsByEmail(email);
        assertFalse(exists);
    }

    @Test
    void shouldGetUserByHisUsername(){
        String username = "demo";
        User user = User
                .builder()
                .username(username)
                .email("demo@mail.com")
                .build();
        userRepository.save(user);

        Optional<User> result = userRepository.findByUsername(username);

        assertTrue(result.isPresent());
    }

    @Test
    void shouldReturnAnEmptyResult(){
        String username = "john";
        User user = User
                .builder()
                .username("demo")
                .email("demo@mail.com")
                .build();
        userRepository.save(user);

        Optional<User> result = userRepository.findByUsername(username);

        assertTrue(result.isEmpty());
    }
}