package com.connect.friendship.domain.repository;

import com.connect.friendship.domain.entities.FriendshipRequest;
import com.connect.user.domain.entities.PostgresUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendshipRequestRepository extends JpaRepository<FriendshipRequest, Long> {
    Optional<FriendshipRequest> findAllByRequestedTo(PostgresUser user);
}
