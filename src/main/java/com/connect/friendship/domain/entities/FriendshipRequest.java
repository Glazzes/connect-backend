package com.connect.friendship.domain.entities;

import com.connect.user.domain.entities.PostgresUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "friend_requests")
@NoArgsConstructor
public class FriendshipRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private PostgresUser requestedBy;

    @OneToOne
    private PostgresUser requestedTo;

}
