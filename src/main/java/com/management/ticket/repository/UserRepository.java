package com.management.ticket.repository;

import com.management.ticket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @création 10/10/2024
 * @projet ticket-management
 * @auteur tsyta.diallo
 * @package com.management.ticket.repository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT CASE WHEN COUNT(u)>0 THEN true ELSE false END FROM User u WHERE u.username=:username")
    boolean existsByUsername(String username);

    @Query("SELECT CASE WHEN COUNT(u)>0 THEN true ELSE false END FROM User u WHERE u.email=:email")
    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.username=:username")
    Optional<User> findByUsername(String username);
}
