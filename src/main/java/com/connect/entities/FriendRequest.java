package com.connect.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Table(name = "friend_requests")
@NoArgsConstructor
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User requestedBy;

    @OneToOne
    private User requestedTo;

}
