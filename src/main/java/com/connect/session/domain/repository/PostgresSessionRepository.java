package com.connect.session.domain.repository;

import com.connect.session.domain.entities.PostgresSession;
import com.connect.user.domain.entities.PostgresUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostgresSessionRepository extends JpaRepository<PostgresSession, Long> {
    List<PostgresSession> findAllByAssociatedUser(PostgresUser user);
}
