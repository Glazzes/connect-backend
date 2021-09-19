package com.connect.friendship.domain.entities;

import com.connect.user.domain.entities.PostgresUser;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "friend_requests")
@NoArgsConstructor
public class FriendRequest {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
            name = "uuid",
            strategy = "org.hibernate.id.UUIDHexGenerator"
    )
    private String id;

    @ManyToOne
    private PostgresUser requestedBy;

    @ManyToOne
    private PostgresUser requestedTo;

    public FriendRequest(PostgresUser requestedBy, PostgresUser requestedTo){
        this.requestedBy = requestedBy;
        this.requestedTo = requestedTo;
    }

}
