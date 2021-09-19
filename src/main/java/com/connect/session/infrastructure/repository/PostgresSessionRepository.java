package com.connect.session.infrastructure.repository;

import com.connect.session.domain.entities.PostgresSession;
import com.connect.user.domain.entities.PostgresUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostgresSessionRepository extends JpaRepository<PostgresSession, String> {
    List<PostgresSession> findAllByOwner(PostgresUser user);
    boolean existsById(String id);
}
