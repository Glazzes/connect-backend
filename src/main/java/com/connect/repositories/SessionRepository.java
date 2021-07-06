package com.connect.repositories;

import com.connect.entities.Session;
import com.connect.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findAllByAssociatedUser(User user);
}
