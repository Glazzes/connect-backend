package com.connect.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"friends"})
@EqualsAndHashCode(exclude = {"friends"})
public class User {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
            name = "uuid",
            strategy = "org.hibernate.id.UUIDHexGenerator"
    )
    private String id;

    @Column(name = "username", unique = true, nullable = false, length = 100)
    private String username;

    @Column(name = "nickname", nullable = false, length = 100)
    private String nickname;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "profile_picture", nullable = false)
    private String profilePicture;

    @OneToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = {@JoinColumn(name = "fk_user_id")},
            inverseJoinColumns = {@JoinColumn(name = "fk_friend_id")}
    )
    private Set<User> friends = new HashSet<>();

    public User(String username, String email, String password){
        this.username = username;
        this.nickname = username;
        this.email = email;
        this.password = password;
        this.profilePicture = "https://randomuser.me/api/portraits/men/44.jpg";
    }

}
