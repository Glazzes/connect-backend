package com.connect.friendship.domain.repository;

import com.connect.friendship.domain.entities.FriendRequest;
import com.connect.user.domain.entities.PostgresUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, String> {
    Long countAllByRequestedTo(PostgresUser user);
    List<FriendRequest> findByRequestedTo(PostgresUser user);
    List<FriendRequest> findByRequestedBy(PostgresUser user);
}
