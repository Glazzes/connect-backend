package com.connect.user.infrastructure.repository;

import com.connect.user.domain.entities.PostgresUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostgresUserRepository extends JpaRepository<PostgresUser, String> {
    Optional<PostgresUser> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
